package msu.ghostloyz.socialMedia.repo.implementations;

import lombok.extern.slf4j.Slf4j;
import msu.ghostloyz.socialMedia.model.Post;
import msu.ghostloyz.socialMedia.repo.interfaces.FeedDAO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
@Slf4j
public class FeedDAOImpl1 implements FeedDAO {
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
    public List<Post> getNewsFeedWithFilters(Integer userId, boolean isFriends, boolean isSubscriptions, boolean noCategory, String category) {
        List<Post> postList = new ArrayList<>();
        if (isFriends) {
            postList.addAll(getFriendsPosts(userId));
        }
        if (isSubscriptions) {
            postList.addAll(getSubscriptionsPosts(userId));
        }
        if (!noCategory) {
            postList = postList.stream().filter((post) -> post.getCategory().equals(category)).toList();
        }
        return postList;
    }

    private List<Post> getFriendsPosts(Integer userId) {
        List<Post> postList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM posts WHERE EXISTS(SELECT * FROM friends WHERE friends.user1_id = posts.author_id AND friends.user2_id = ?) AND EXISTS(SELECT * FROM friends WHERE friends.user1_id = ? AND friends.user2_id = posts.author_id)");
            executeAndBuild(userId, postList, preparedStatement);
        } catch (SQLException e) {
            log.error("Не удалось получить посты друзей в методе getFriendsPosts", e);
        }
        return postList;
    }

    private List<Post> getSubscriptionsPosts(Integer userId) {
        List<Post> postList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM posts WHERE NOT EXISTS(SELECT * FROM friends WHERE friends.user1_id = posts.author_id AND friends.user2_id = ?) AND EXISTS(SELECT * FROM friends WHERE friends.user1_id = ? AND friends.user2_id = posts.author_id)");
            executeAndBuild(userId, postList, preparedStatement);
        } catch (SQLException e) {
            log.error("Не удалось получить посты подписок в методе getSubscriptionsPosts", e);
        }
        return postList;
    }

    private void executeAndBuild(Integer userId, List<Post> postList, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Integer id = resultSet.getInt("id");
            Integer authorId = resultSet.getInt("author_id");
            String descrip = resultSet.getString("descrip");
            Integer likeCnt = resultSet.getInt("like_cnt");
            Integer dislikeCnt = resultSet.getInt("dislike_cnt");
            Integer repostCnt = resultSet.getInt("repost_cnt");
            Integer comCnt = resultSet.getInt("com_cnt");
            Timestamp crDate = resultSet.getTimestamp("cr_date");
            String category = resultSet.getString("category");
            postList.add(Post.builder()
                    .id(id)
                    .authorId(authorId)
                    .descrip(descrip)
                    .likeCnt(likeCnt)
                    .dislikeCnt(dislikeCnt)
                    .repostCnt(repostCnt)
                    .comCnt(comCnt)
                    .crDate(crDate)
                    .category(category)
                    .build()
            );
        }
    }
}
