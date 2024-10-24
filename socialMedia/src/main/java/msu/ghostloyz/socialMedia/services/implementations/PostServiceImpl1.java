package msu.ghostloyz.socialMedia.services.implementations;

import msu.ghostloyz.socialMedia.model.Post;
import msu.ghostloyz.socialMedia.repo.interfaces.PostDAO;
import msu.ghostloyz.socialMedia.services.interfaces.PostService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.HashMap;
import java.util.List;

@Service
public class PostServiceImpl1 implements PostService {
    private final PostDAO repository;

    public PostServiceImpl1(final PostDAO repository) {
        this.repository = repository;
    }

    @Override
    public List<Post> getPostsByAuthorId(Integer authorId) {
        return repository.getPostsByAuthorId(authorId);
    }

    @Override
    public List<Post> getPostsByAuthorIdList(List<Integer> authorIdList) {
        return repository.getPostsByAuthorIdList(authorIdList);
    }

    @Override
    public Integer addPost(Post post) {
        return repository.addPost(post);
    }

    @Override
    public void addImagesToPostById(Integer postId, List<MultipartFile> imageFiles) {
        repository.addImagesToPostById(postId, imageFiles);
    }

    @Override
    public void likePost(Integer postId) {
        repository.likePost(postId);
    }

    @Override
    public void dislikePost(Integer postId) {
        repository.dislikePost(postId);
    }

    @Override
    public void deletePost(Integer postId) {
        repository.deletePost(postId);
    }

    @Override
    public Post getPostById(Integer postId) {
        return repository.getPostById(postId);
    }

    @Override
    public HashMap<Integer, List<Integer>> getPostImagesIdByUserId(Integer userId) {
        return repository.getPostImagesIdByUserId(userId);
    }

    @Override
    public HashMap<Integer, List<Integer>> getPostImagesIdByPostIdList(List<Integer> postIdList) {
        return repository.getPostImagesIdByPostIdList(postIdList);
    }


    @Override
    public Blob getPostImageById(Integer imageId) {
        return repository.getPostImageById(imageId);
    }
}
