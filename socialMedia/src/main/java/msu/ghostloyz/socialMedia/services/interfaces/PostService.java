package msu.ghostloyz.socialMedia.services.interfaces;

import msu.ghostloyz.socialMedia.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.HashMap;
import java.util.List;

public interface PostService {
    //Get
    Post getPostById(Integer postId);
    List<Post> getPostsByAuthorId(Integer authorId);
    List<Post> getPostsByAuthorIdList(List<Integer> authorIdList);
    Blob getPostImageById(Integer imageId);
    HashMap<Integer, List<Integer>> getPostImagesIdByUserId(Integer userId);
    HashMap<Integer, List<Integer>> getPostImagesIdByPostIdList(List<Integer> postIdList);

    //Post
    Integer addPost(Post post);
    void addImagesToPostById(Integer postId, List<MultipartFile> imageFiles);

    //Put
    void likePost(Integer postId);
    void dislikePost(Integer postId);

    //Delete
    void deletePost(Integer postId);

}
