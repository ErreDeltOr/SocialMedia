package msu.ghostloyz.socialMedia.controllers;

import lombok.AllArgsConstructor;
import msu.ghostloyz.socialMedia.model.Post;
import msu.ghostloyz.socialMedia.services.interfaces.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/social_media/userProfile")
public class PostController {
    private final PostService postService;

    //PostMapping
    @PostMapping("/addPost")
    public String addPost(@RequestParam("id") Integer authorId, @RequestParam("imageFiles") MultipartFile[] imageFiles,
                          @RequestParam("descrip") String descrip, @RequestParam("category") String category) {

        Integer postId = postService.addPost(Post.builder()
                .authorId(authorId)
                .descrip(descrip)
                .likeCnt(0)
                .dislikeCnt(0)
                .repostCnt(0)
                .comCnt(0)
                .crDate(new Timestamp(System.currentTimeMillis()))
                .category(category)
                .build()
        );
        postService.addImagesToPostById(postId, new ArrayList<>(Arrays.asList(imageFiles)));
        return "redirect:/api/v1/social_media/userProfile/" + authorId;
    }

    //PutMapping
    @PostMapping("/likePost")
    public String likePost(@RequestParam("postId") Integer postId, @RequestParam("authorId") Integer authorId) {
        postService.likePost(postId);
        return "redirect:/api/v1/social_media/userProfile/" + authorId;
    }

    //PutMapping
    @PostMapping("/dislikePost")
    public String dislikePost(@RequestParam("postId") Integer postId, @RequestParam("authorId") Integer authorId) {
        postService.dislikePost(postId);
        return "redirect:/api/v1/social_media/userProfile/" + authorId;
    }

    //DeleteMapping
    @PostMapping("/deletePost")
    public String deletePost(@RequestParam("postId") Integer postId, @RequestParam("authorId") Integer authorId) {
        postService.deletePost(postId);
        return "redirect:/api/v1/social_media/userProfile/" + authorId;
    }
}
