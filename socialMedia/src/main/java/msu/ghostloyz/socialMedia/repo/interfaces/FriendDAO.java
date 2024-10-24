package msu.ghostloyz.socialMedia.repo.interfaces;

import msu.ghostloyz.socialMedia.model.User;

import java.util.List;

public interface FriendDAO {
    //Get
    List<User> getFriendsByUserId(Integer userId);
    List<User> getBlackListByUserId(Integer userId);
    List<User> getFollowersByUserId(Integer userId);
    List<User> getFriendRequestsByUserId(Integer userId);

    //Post
    void addFriend(Integer userId1, Integer userId2);
    void addToBlackList(Integer userId1, Integer userId2);
    void deleteFriendRequest(Integer userId, Integer followerId);

    //Delete
    void deleteFriend(Integer userId1, Integer userId2);
    void deleteFromBlackList(Integer userId1, Integer userId2);

    boolean isFriends(Integer userId1, Integer userId2);
    boolean isFollower(Integer userId1, Integer userId2);
}
