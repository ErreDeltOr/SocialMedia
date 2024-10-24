package msu.ghostloyz.socialMedia.services.implementations;

import lombok.RequiredArgsConstructor;
import msu.ghostloyz.socialMedia.model.Post;
import msu.ghostloyz.socialMedia.repo.interfaces.FeedDAO;
import msu.ghostloyz.socialMedia.services.interfaces.FeedService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl1 implements FeedService {
    private final FeedDAO repository;
    @Override
    public List<Post> getNewsFeedWithFilters(Integer userId, boolean isFriends, boolean isSubscriptions, boolean noCategory, String category) {
        return repository.getNewsFeedWithFilters(userId, isFriends, isSubscriptions, noCategory, category);
    }
}
