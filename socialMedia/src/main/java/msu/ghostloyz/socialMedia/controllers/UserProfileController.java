package msu.ghostloyz.socialMedia.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msu.ghostloyz.socialMedia.services.interfaces.FriendService;
import msu.ghostloyz.socialMedia.services.interfaces.PostService;
import msu.ghostloyz.socialMedia.services.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/api/v1/social_media")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {
    private final PostService postService;
    private final UserService userService;
    private final FriendService friendService;

    @GetMapping("/allUsers")
    public String getAllUsers(Model model, Principal principal) {
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("authId", authId);
        return "allUsers";
    }

    @GetMapping("/userProfile/{id}")
    public String getUserById(@PathVariable("id") Integer userId, Model model, Principal principal) {
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        model.addAttribute("isFollowerMe", friendService.isFollower(authId, userId));
        model.addAttribute("isFollowerOther", friendService.isFollower(userId, authId));
        model.addAttribute("isFriends", friendService.isFriends(userId, authId));
        model.addAttribute("friendList", friendService.getFriendsByUserId(userId));
        model.addAttribute("followerList", friendService.getFollowersByUserId(userId));
        model.addAttribute("friendRequestList", friendService.getFriendRequestsByUserId(userId));
        model.addAttribute("authId", authId);
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("userImagesIdList", userService.getUserImagesIdByUserId(userId));
        model.addAttribute("postList", postService.getPostsByAuthorId(userId));
        model.addAttribute("postImagesIdList", postService.getPostImagesIdByUserId(userId));
        return "userProfile";
    }

    //PostMapping
    @PostMapping("/userProfile/addProfilePhoto")
    public String addProfilePhoto(@RequestParam("userId") Integer userId,
                                  @RequestParam("profilePhoto") MultipartFile profilePhoto) {
        try {
            userService.addProfileImage(userId, profilePhoto);
        }
        catch (Exception e) {
            log.error("Не удалось добавить фото в профиль. Эндпоинт: " + "/api/v1/social_media/userProfile/addProfilePhoto", e);
        }
        return "redirect:/api/v1/social_media/userProfile/" + userId;
    }

    //PutMapping
    @PostMapping("/userProfile/changeProfilePhoto")
    public String changeProfilePhoto(@RequestParam("userId") Integer userId,
                                  @RequestParam("profilePhoto") MultipartFile profilePhoto) {
        try {
            userService.changeProfileImage(userId, profilePhoto);
        }
        catch (Exception e) {
            log.error("Не удалось изменить фото профиля. Эндпоинт: " + "/api/v1/social_media/userProfile/changeProfilePhoto", e);
        }
        return "redirect:/api/v1/social_media/userProfile/" + userId;
    }

}
