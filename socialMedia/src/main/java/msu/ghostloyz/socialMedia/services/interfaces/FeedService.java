package msu.ghostloyz.socialMedia.services.interfaces;

import msu.ghostloyz.socialMedia.model.Post;

import java.util.List;

public interface FeedService {
    List<Post> getNewsFeedWithFilters(Integer userId, boolean isFriends, boolean isSubscriptions, boolean noCategory, String category);
}
