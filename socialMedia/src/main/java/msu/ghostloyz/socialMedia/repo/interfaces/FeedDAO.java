package msu.ghostloyz.socialMedia.repo.interfaces;

import msu.ghostloyz.socialMedia.model.Post;

import java.util.List;

public interface FeedDAO {
    List<Post> getNewsFeedWithFilters(Integer userId, boolean isFriends, boolean isSubscriptions, boolean noCategory, String category);
}
