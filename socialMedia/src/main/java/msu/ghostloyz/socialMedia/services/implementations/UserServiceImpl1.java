package msu.ghostloyz.socialMedia.services.implementations;

import lombok.RequiredArgsConstructor;
import msu.ghostloyz.socialMedia.model.User;
import msu.ghostloyz.socialMedia.model.enums.Role;
import msu.ghostloyz.socialMedia.repo.interfaces.UserDAO;
import msu.ghostloyz.socialMedia.services.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl1 implements UserService {
    private final UserDAO repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Integer userId) {
        return repository.getUserById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    @Override
    public Blob getUserImageById(Integer imageId) {
        return repository.getUserImageById(imageId);
    }

    @Override
    public List<Integer> getUserImagesIdByUserId(Integer userId) {
        return repository.getUserImagesIdByUserId(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    @Override
    public void addProfileImage(Integer userId, MultipartFile image) throws IOException, SQLException {
        repository.addProfileImage(userId, image);
    }

    @Override
    public void changeProfileImage(Integer userId, MultipartFile image) throws IOException, SQLException {
        repository.changeProfileImage(userId, image);
    }

    @Override
    public boolean createUser(User user) {
        String email = user.getEmail();
        if (repository.getUserByEmail(email) != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(new HashSet<>());
        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.createUser(user);
    }
}
