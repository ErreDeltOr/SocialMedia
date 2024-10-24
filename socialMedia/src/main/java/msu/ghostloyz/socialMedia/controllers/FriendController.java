package msu.ghostloyz.socialMedia.controllers;

import lombok.AllArgsConstructor;
import msu.ghostloyz.socialMedia.services.interfaces.FriendService;
import msu.ghostloyz.socialMedia.services.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/social_media/userProfile")
public class FriendController {
    private final UserService userService;
    private final FriendService friendService;

    @GetMapping("/friends/{id}")
    public String getFriends(@PathVariable("id") Integer userId, Model model) {
        model.addAttribute("friendList", friendService.getFriendsByUserId(userId));
        model.addAttribute("userId", userId);
        return "friends";
    }

    @GetMapping("/followers/{id}")
    public String getFollowers(@PathVariable("id") Integer userId, Model model) {
        model.addAttribute("followerList", friendService.getFollowersByUserId(userId));
        model.addAttribute("userId", userId);
        return "followers";
    }

    @GetMapping("/friendRequests/{id}")
    public String getFriendsRequests(@PathVariable("id") Integer userId, Model model) {
        model.addAttribute("friendRequestList", friendService.getFriendRequestsByUserId(userId));
        model.addAttribute("userId", userId);
        return "friendRequests";
    }

    //PostMapping
    @PostMapping("/addFriend")
    public String  addFriend(@RequestParam("userId") Integer userId, Principal principal) {
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        friendService.addFriend(authId, userId);
        return "redirect:/api/v1/social_media/userProfile/" + userId;
    }

    //DeleteMapping
    @PostMapping("/deleteFriend")
    public String  deleteFriend(@RequestParam("userId") Integer userId, Principal principal) {
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        friendService.deleteFriend(authId, userId);
        return "redirect:/api/v1/social_media/userProfile/" + userId;
    }

    //PostMapping
    @PostMapping("/friendRequests/acceptFriendRequest")
    public String  acceptFriendRequest(@RequestParam("userId") Integer userId, Principal principal) {
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        friendService.addFriend(authId, userId);
        return "redirect:/api/v1/social_media/userProfile/friendRequests/" + authId;
    }

    //DeleteMapping
    @PostMapping("/friendRequests/deleteFriendRequest")
    public String  deleteFriendRequest(@RequestParam("userId") Integer userId, Principal principal) {
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        friendService.deleteFriendRequest(authId, userId);
        return "redirect:/api/v1/social_media/userProfile/friendRequests/" + authId;
    }
}
