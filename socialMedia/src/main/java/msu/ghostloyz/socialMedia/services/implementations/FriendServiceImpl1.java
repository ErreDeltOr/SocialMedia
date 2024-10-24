package msu.ghostloyz.socialMedia.services.implementations;

import lombok.RequiredArgsConstructor;
import msu.ghostloyz.socialMedia.model.User;
import msu.ghostloyz.socialMedia.repo.interfaces.FriendDAO;
import msu.ghostloyz.socialMedia.services.interfaces.FriendService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl1 implements FriendService {
    private final FriendDAO repository;

    @Override
    public List<User> getFriendsByUserId(Integer userId) {
        return repository.getFriendsByUserId(userId);
    }

    @Override
    public List<User> getBlackListByUserId(Integer userId) {
        return repository.getBlackListByUserId(userId);
    }

    @Override
    public List<User> getFollowersByUserId(Integer userId) {
        return repository.getFollowersByUserId(userId);
    }

    @Override
    public List<User> getFriendRequestsByUserId(Integer userId) {
        return repository.getFriendRequestsByUserId(userId);
    }

    @Override
    public void deleteFriend(Integer userId1, Integer userId2) {
        repository.deleteFriend(userId1, userId2);
    }

    @Override
    public void deleteFromBlackList(Integer userId1, Integer userId2) {
        repository.deleteFromBlackList(userId1, userId2);
    }

    @Override
    public boolean isFriends(Integer userId1, Integer userId2) {
        return repository.isFriends(userId1, userId2);
    }

    @Override
    public boolean isFollower(Integer userId1, Integer userId2) {
        return repository.isFollower(userId1, userId2);
    }

    @Override
    public void addFriend(Integer userId1, Integer userId2) {
        repository.addFriend(userId1, userId2);
    }

    @Override
    public void addToBlackList(Integer userId1, Integer userId2) {
        repository.addToBlackList(userId1, userId2);
    }

    @Override
    public void deleteFriendRequest(Integer userId, Integer followerId) {
        repository.deleteFriendRequest(userId, followerId);
    }
}
