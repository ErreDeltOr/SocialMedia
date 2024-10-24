package msu.ghostloyz.socialMedia.controllers;

import lombok.AllArgsConstructor;
import msu.ghostloyz.socialMedia.model.Comment;
import msu.ghostloyz.socialMedia.model.User;
import msu.ghostloyz.socialMedia.services.interfaces.CommentService;
import msu.ghostloyz.socialMedia.services.interfaces.PostService;
import msu.ghostloyz.socialMedia.services.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/social_media/userProfile")
public class CommentController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/{userId}/postComments/{postId}")
    public String getComments(@PathVariable("postId") Integer postId,
                              @PathVariable("userId") Integer userId, Principal principal, Model model) {
        List<Comment> commentList = commentService.getCommentsByPostId(postId);
        HashMap<Integer, User> commentAuthorsMap = new HashMap<>();
        for (Comment comment: commentList) {
            commentAuthorsMap.put(comment.getAuthorId(), userService.getUserById(comment.getAuthorId()));
        }
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        model.addAttribute("commentList", commentList);
        model.addAttribute("commentAuthorsMap", commentAuthorsMap);
        model.addAttribute("post", postService.getPostById(postId));
        model.addAttribute("postImagesId", postService.getPostImagesIdByUserId(userId).get(postId));
        model.addAttribute("postAuthorId", userId);
        model.addAttribute("authId", authId);
        return "comments";
    }

    //PostMapping
    @PostMapping("/postComments/addComment")
    public String addComment(@RequestParam("postId") Integer postId, @RequestParam("text") String text,
                             @RequestParam("postAuthorId") Integer postAuthorId, Principal principal) {
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        commentService.addComment(Comment.builder()
                .authorId(authId)
                .postId(postId)
                .text(text)
                .crDate(new Timestamp(System.currentTimeMillis()))
                .build());
        return "redirect:/api/v1/social_media/userProfile/" + postAuthorId + "/postComments/" + postId;
    }

    //DeleteMapping
    @PostMapping("/postComments/deleteComment")
    public String deleteComment(@RequestParam("commentId") Integer commentId, @RequestParam("postAuthorId") Integer postAuthorId,
                                @RequestParam("postId") Integer postId) {
        commentService.deleteComment(commentId);
        return "redirect:/api/v1/social_media/userProfile/" + postAuthorId + "/postComments/" + postId;
    }
}
