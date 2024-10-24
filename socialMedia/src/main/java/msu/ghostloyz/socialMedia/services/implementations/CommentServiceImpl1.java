package msu.ghostloyz.socialMedia.services.implementations;

import lombok.RequiredArgsConstructor;
import msu.ghostloyz.socialMedia.model.Comment;
import msu.ghostloyz.socialMedia.repo.interfaces.CommentDAO;
import msu.ghostloyz.socialMedia.services.interfaces.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl1 implements CommentService {
    private final CommentDAO repository;
    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        return repository.getCommentsByPostId(postId);
    }

    @Override
    public void addComment(Comment comment) {
        repository.addComment(comment);
    }

    @Override
    public void deleteComment(Integer commentId) {
        repository.deleteComment(commentId);
    }
}
