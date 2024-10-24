package msu.ghostloyz.socialMedia.services.interfaces;

import msu.ghostloyz.socialMedia.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    //Get
    User getUserById(Integer userId);
    List<User> getAllUsers();
    Blob getUserImageById(Integer imageId);
    List<Integer> getUserImagesIdByUserId(Integer userId);
    User getUserByEmail(String email);

    //Post
    void addProfileImage(Integer userId, MultipartFile image) throws IOException, SQLException;
    boolean createUser(User user);

    //Put
    void changeProfileImage(Integer userId, MultipartFile image) throws IOException, SQLException;

    //Delete
}
