package msu.ghostloyz.socialMedia.services.interfaces;

import msu.ghostloyz.socialMedia.model.Comment;

import java.util.List;

public interface CommentService {
    //Get
    List<Comment> getCommentsByPostId(Integer postId);

    //Post
    void addComment(Comment comment);

    //Put

    //Delete
    void deleteComment(Integer commentId);
}
