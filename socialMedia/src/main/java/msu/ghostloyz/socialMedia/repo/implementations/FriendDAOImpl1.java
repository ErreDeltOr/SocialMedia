package msu.ghostloyz.socialMedia.repo.implementations;

import lombok.extern.slf4j.Slf4j;
import msu.ghostloyz.socialMedia.model.User;
import msu.ghostloyz.socialMedia.repo.interfaces.FriendDAO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class FriendDAOImpl1 implements FriendDAO {
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
    public void deleteFriend(Integer userId1, Integer userId2) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM friends WHERE user1_id = ? AND user2_id = ?");
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(
                    "UPDATE friends SET is_declined = 1 WHERE user1_id = ? AND user2_id = ?");
            preparedStatement.setInt(1, userId2);
            preparedStatement.setInt(2, userId1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось удалить друга в методе deleteFriend()", e);
        }
    }

    @Override
    public void deleteFromBlackList(Integer userId1, Integer userId2) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM blacklist WHERE user1_id = ? AND user2_id = ?");
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось удалить из чёрного списка в методе deleteFromBlackList()", e);
        }
    }

    @Override
    public void addFriend(Integer userId1, Integer userId2) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO friends (user1_id, user2_id) VALUES(?, ?)");
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(
                    "UPDATE friends SET is_declined = 0 WHERE user1_id = ? AND user2_id = ?");
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось добавить друга в методе addFriend()", e);
        }
    }

    @Override
    public void addToBlackList(Integer userId1, Integer userId2) {
        this.deleteFriend(userId1, userId2);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO blacklist (user1_id, user2_id) VALUES(?, ?)");
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось добавить пользователя в чёрный список в методе addToBlackList()", e);
        }
    }

    @Override
    public void deleteFriendRequest(Integer userId, Integer followerId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE friends SET is_declined = 1 WHERE user1_id = ? AND user2_id = ?");
            preparedStatement.setInt(1, followerId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось удалить запрос в друзья в методе deleteFriendRequest()", e);
        }
    }

    @Override
    public boolean isFriends(Integer userId1, Integer userId2) {
        boolean flag = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM friends WHERE EXISTS (SELECT * FROM friends WHERE friends.user1_id = ? AND friends.user2_id = ?) AND EXISTS (SELECT * FROM friends WHERE friends.user1_id = ? AND friends.user2_id = ?)");
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.setInt(3, userId2);
            preparedStatement.setInt(4, userId1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            log.error("Не удалось выяснить, являются ли пользователи друзьями в методе isFriends()", e);
        }
        return flag;
    }

    public boolean isFollower(Integer userId1, Integer userId2) {
        boolean flag = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM friends WHERE EXISTS (SELECT * FROM friends WHERE friends.user1_id = ? AND friends.user2_id = ?)");
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            log.error("Не удалось выяснить, являются ли пользователь подписчиком другого в методе isFollowers()", e);
        }
        return flag;
    }


    @Override
    public List<User> getFriendsByUserId(Integer userId) {
        List<User> friendList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users JOIN (SELECT * FROM friends WHERE user1_id = ?) as fl ON users.id = fl.user2_id WHERE EXISTS (SELECT * FROM friends WHERE friends.user1_id = users.id AND friends.user2_id = ?);");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Integer age = resultSet.getInt("age");
                friendList.add(User.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .age(age)
                        .build()
                );
            }
        } catch (SQLException e) {
            log.error("Не удалось получить друзей по id в методе getFriendsByUserId()", e);
        }
        return friendList;
    }

    @Override
    public List<User> getBlackListByUserId(Integer userId) {
        List<User> blackList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users JOIN (SELECT * FROM blacklist WHERE user1_id = ?) as bl ON users.id = bl.user2_id");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Integer age = resultSet.getInt("age");
                blackList.add(User.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .age(age)
                        .build()
                );
            }
        } catch (SQLException e) {
            log.error("Не удалось получить чёрный список по id в методе getBlackListByUserId()", e);
        }
        return blackList;
    }

    @Override
    public List<User> getFollowersByUserId(Integer userId) {
        List<User> followerList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users JOIN friends ON users.id = friends.user1_id WHERE EXISTS (SELECT * FROM friends as f WHERE f.user1_id = users.id AND f.user2_id = ?) AND NOT EXISTS (SELECT * FROM  friends as f WHERE f.user1_id = ? AND f.user2_id = users.id) AND friends.user2_id = ?;");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Integer age = resultSet.getInt("age");
                followerList.add(User.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .age(age)
                        .build()
                );
            }
        } catch (SQLException e) {
            log.error("Не удалось получить подписчиков по id в методе getFollowersByUserId()", e);
        }
        return followerList;
    }

    @Override
    public List<User> getFriendRequestsByUserId(Integer userId) {
        List<User> friendRequestList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users JOIN friends ON users.id = friends.user1_id WHERE EXISTS (SELECT * FROM friends as f WHERE f.user1_id = users.id AND f.user2_id = ?) AND NOT EXISTS (SELECT * FROM  friends as f WHERE f.user1_id = ? AND f.user2_id = users.id) AND friends.user2_id = ? AND friends.is_declined = 0;");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Integer age = resultSet.getInt("age");
                friendRequestList.add(User.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .age(age)
                        .build()
                );
            }
        } catch (SQLException e) {
            log.error("Не удалось получить список запросов в друзья по id в методе getFriendRequestsByUserId()", e);
        }
        return friendRequestList;
    }
}
