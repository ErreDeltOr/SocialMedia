package msu.ghostloyz.socialMedia.repo.implementations;

import lombok.extern.slf4j.Slf4j;
import msu.ghostloyz.socialMedia.model.Comment;
import msu.ghostloyz.socialMedia.repo.interfaces.CommentDAO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class CommentDAOImpl1 implements CommentDAO {
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
    public List<Comment> getCommentsByPostId(Integer postId) {
        List<Comment> comList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM comments  WHERE comments.post_id = ?");
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer authorId = resultSet.getInt("com_author_id");
                String text = resultSet.getString("com_text");
                Timestamp timestamp = resultSet.getTimestamp("cr_date");
                comList.add(Comment.builder()
                        .id(id)
                        .authorId(authorId)
                        .postId(postId)
                        .text(text)
                        .crDate(timestamp)
                        .build()
                );
            }
        } catch (SQLException e) {
            log.error("Не удалось получить комментарий по id поста в методе getCommentsByPostId()", e);
        }
        return comList;
    }

    @Override
    public void addComment(Comment comment) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO comments (post_id, com_author_id, com_text, cr_date) VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, comment.getPostId());
            preparedStatement.setInt(2, comment.getAuthorId());
            preparedStatement.setString(3, comment.getText());
            preparedStatement.setTimestamp(4, comment.getCrDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось добавить комментарий в методе addComment", e);
        }
    }

    @Override
    public void deleteComment(Integer commentId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM comments WHERE id = ?");
            preparedStatement.setInt(1, commentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось удалить комментарий в методе deleteComment()", e);
        }
    }
}
