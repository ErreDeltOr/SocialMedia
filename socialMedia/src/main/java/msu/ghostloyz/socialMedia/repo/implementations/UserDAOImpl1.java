package msu.ghostloyz.socialMedia.repo.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msu.ghostloyz.socialMedia.model.User;
import msu.ghostloyz.socialMedia.model.enums.Role;
import msu.ghostloyz.socialMedia.repo.interfaces.UserDAO;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDAOImpl1 implements UserDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/msu_social_media";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345";
    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            log.error("Не удалось подключиться к базе данных", e);
        }
    }

    @Override
    public User getUserById(Integer userId) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE users.id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Integer age = resultSet.getInt("age");
                //Blob profileImage = resultSet.getBlob("profile_image");
                user = User.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .age(age)
                        .build();
            }
        } catch (SQLException e) {
            log.error("Не удалось получить пользователя по id в методе getUserById()", e);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Integer age = resultSet.getInt("age");
                //Blob profileImage = resultSet.getBlob("profile_image");
                userList.add(User.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .age(age)
                        .build()
                );
            }
        } catch (SQLException e) {
            log.error("Не удалось получить пользователей в методе getAllUsers()", e);
        }
        return userList;
    }

    @Override
    public Blob getUserImageById(Integer imageId) {
        Blob userImage = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT user_photo FROM photo_to_users  WHERE id = ?");
            preparedStatement.setInt(1, imageId);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            userImage = resultSet.getBlob("user_photo");
        } catch (SQLException e) {
            log.error("Не удалось получить изображение по id в методе getUserImageById()", e);
        }
        return userImage;
    }

    @Override
    public List<Integer> getUserImagesIdByUserId(Integer userId) {
        List<Integer> imageIdList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM photo_to_users  WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Integer imageId = resultSet.getInt("id");
                imageIdList.add(imageId);
            }
        } catch (SQLException e) {
            log.error("Не удалось получить список id фото по id в методе getUserImagesIdByUserId()", e);
        }
        return imageIdList;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users  WHERE email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Integer age = resultSet.getInt("age");
                boolean active = resultSet.getBoolean("active");

                preparedStatement = connection.prepareStatement(
                        "SELECT role FROM roles_to_users  WHERE user_id = ?"
                );
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                Set<Role> roles = new HashSet<>();
                while (resultSet.next()) {
                    roles.add(Role.valueOf(resultSet.getString("role")));
                }

                user = User.builder()
                        .id(id)
                        .email(email)
                        .password(password)
                        .firstName(firstName)
                        .lastName(lastName)
                        .age(age)
                        .roles(roles)
                        .active(active)
                        .build();
            }
        } catch (SQLException e) {
            log.error("Не удалось получить пользователя по почте в методе getUserByEmail()", e);
        }
        return user;
    }

    @Override
    public void addProfileImage(Integer userId, MultipartFile image) throws IOException, SQLException {
        Blob profilePhoto = new SerialBlob(image.getBytes());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO photo_to_users (user_id, user_photo, is_profile_image) VALUES(?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setBlob(2, profilePhoto);
            preparedStatement.setBoolean(3, true);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось добавить фото в профиль в методе addProfileImage()", e);
        }
    }

    @Override
    public void changeProfileImage(Integer userId, MultipartFile image) throws IOException, SQLException {
        Blob profilePhoto = new SerialBlob(image.getBytes());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE photo_to_users SET user_photo = ? WHERE user_id = ? AND is_profile_image = 1");
            preparedStatement.setBlob(1, profilePhoto);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось поменять фото в профиль в методе addProfileImage()", e);
        }
    }

    @Override
    public boolean createUser(User user) {
        int userId;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users (email, password, first_name, last_name, age, active)  VALUES(?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setInt(5, user.getAge());
            preparedStatement.setBoolean(6, user.getActive());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            userId = resultSet.getInt(1);

            preparedStatement = connection.prepareStatement(
                    "INSERT INTO roles_to_users (user_id, role) VALUES(?, ?)"
            );
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, Role.ROLE_USER.name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось создать пользователя в методе createUser()", e);
        }
        return true;
    }
}
