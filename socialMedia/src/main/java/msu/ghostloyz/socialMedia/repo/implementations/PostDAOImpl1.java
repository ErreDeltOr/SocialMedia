package msu.ghostloyz.socialMedia.repo.implementations;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import msu.ghostloyz.socialMedia.model.Post;
import msu.ghostloyz.socialMedia.repo.interfaces.PostDAO;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class PostDAOImpl1 implements PostDAO {
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
    public List<Post> getPostsByAuthorId(Integer authorId) {
        List<Post> postList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM posts  WHERE posts.author_id = ?");
            preparedStatement.setInt(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
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
        } catch (SQLException e) {
            log.error("Не удалось получить посты по id автора в методе getPostsByAuthorId()", e);
        }
        return postList;
    }

    @Override
    public List<Post> getPostsByAuthorIdList(List<Integer> authorIdList) {
        List<Post> postList = new ArrayList<>();
        for (Integer authorId: authorIdList) {
            postList.addAll(getPostsByAuthorId(authorId));
        }
        return postList;
    }

    @Override
    public Blob getPostImageById(Integer imageId) {
        Blob postImage = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT post_photo FROM photo_to_posts  WHERE id = ?");
            preparedStatement.setInt(1, imageId);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            postImage = resultSet.getBlob("post_photo");
        } catch (SQLException e) {
            log.error("Не удалось получить фото поста по id в методе getPostImageById", e);
        }
        return postImage;
    }

    @Override
    public HashMap<Integer, List<Integer>> getPostImagesIdByUserId(Integer userId) {
        List<Integer> postIdList = this.getPostsByAuthorId(userId).stream().map(Post::getId).toList();
        Map<Integer, List<Integer>> map = postIdList.stream().collect(Collectors.toMap(Function.identity(), PostDAOImpl1::getImagesIdByPostId));
        return new HashMap<>(map);
    }

    @Override
    public HashMap<Integer, List<Integer>> getPostImagesIdByPostIdList(List<Integer> postIdList) {
        Map<Integer, List<Integer>> map = postIdList.stream().collect(Collectors.toMap(Function.identity(), PostDAOImpl1::getImagesIdByPostId));
        return new HashMap<>(map);
    }

    private static List<Integer> getImagesIdByPostId(Integer postId) {
        List<Integer> imageIdList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM photo_to_posts  WHERE post_id = ?");
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                imageIdList.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            log.error("Не удалось получить список id фото по id поста в методе getImagesIdByPostId()", e);
        }
        return imageIdList;
    }


    @Override
    public Integer addPost(Post post) {
        Integer postId = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO posts (author_id, descrip, like_cnt, dislike_cnt, repost_cnt, com_cnt, cr_date, category) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, post.getAuthorId());
            preparedStatement.setString(2, post.getDescrip());
            preparedStatement.setInt(3, post.getLikeCnt());
            preparedStatement.setInt(4, post.getDislikeCnt());
            preparedStatement.setInt(5, post.getRepostCnt());
            preparedStatement.setInt(6, post.getComCnt());
            preparedStatement.setTimestamp(7, post.getCrDate());
            preparedStatement.setString(8, post.getCategory());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            postId = resultSet.getInt(1);

        } catch (SQLException e) {
            log.error("Не удалось добавить пост в методе addPost()", e);
        }
        return postId;
    }


    @SneakyThrows
    @Override
    public void addImagesToPostById(Integer postId, List<MultipartFile> imageFiles) {
        List<Blob> imageBlobFiles = new ArrayList<>();
        for (MultipartFile image: imageFiles) {
            imageBlobFiles.add(new SerialBlob(image.getBytes()));
        }
        try {
            for (Blob image: imageBlobFiles) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO photo_to_posts (post_id, post_photo, is_preview_image) VALUES(?, ?, ?)");
                preparedStatement.setInt(1, postId);
                preparedStatement.setBlob(2, image);
                preparedStatement.setInt(3, 0);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Не удалось добавить список фото к посту по id в методе addImagesToPostById()", e);
        }
    }

    @Override
    public void likePost(Integer postId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE posts SET like_cnt = like_cnt + 1 WHERE id = ?");
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось добавить лайк к посту в методе likePost()", e);
        }
    }

    @Override
    public void dislikePost(Integer postId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE posts SET dislike_cnt = dislike_cnt + 1 WHERE id = ?");
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось добавить дизлайк к посту в методе dislikePost()", e);
        }
    }

    @Override
    public void deletePost(Integer postId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM posts WHERE id = ?");
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Не удалось удалить пост в методе deletePost()", e);
        }
    }


    @Override
    public Post getPostById(Integer postId) {
        Post post = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM posts  WHERE id = ?");
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Integer id = resultSet.getInt("id");
            Integer author_id = resultSet.getInt("author_id");
            String descrip = resultSet.getString("descrip");
            Integer likeCnt = resultSet.getInt("like_cnt");
            Integer dislikeCnt = resultSet.getInt("dislike_cnt");
            Integer repostCnt = resultSet.getInt("repost_cnt");
            Integer comCnt = resultSet.getInt("com_cnt");
            Timestamp crDate = resultSet.getTimestamp("cr_date");
            String category = resultSet.getString("category");
            post = Post.builder()
                    .id(id)
                    .authorId(author_id)
                    .descrip(descrip)
                    .likeCnt(likeCnt)
                    .dislikeCnt(dislikeCnt)
                    .repostCnt(repostCnt)
                    .comCnt(comCnt)
                    .crDate(crDate)
                    .category(category)
                    .build();

        } catch (SQLException e) {
            log.error("Не получить посты подписок в методе getSubscriptionsPosts", e);
        }
        return post;
    }
}
