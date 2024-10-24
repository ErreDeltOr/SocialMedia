package msu.ghostloyz.socialMedia.controllers;

import lombok.RequiredArgsConstructor;
import msu.ghostloyz.socialMedia.model.Post;
import msu.ghostloyz.socialMedia.model.User;
import msu.ghostloyz.socialMedia.services.interfaces.FeedService;
import msu.ghostloyz.socialMedia.services.interfaces.PostService;
import msu.ghostloyz.socialMedia.services.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/social_media")
public class NewsFeedController {
    private final FeedService feedService;
    private final UserService userService;
    private final PostService postService;
    @GetMapping("/feed")
    public String getNewsFeedWithFilters(@RequestParam(name = "isFriends", defaultValue = "false") boolean isFriends,
                                         @RequestParam(name = "isSubscriptions", defaultValue = "false") boolean isSubscriptions,
                                         @RequestParam(name = "category", defaultValue = "") String category,
                                         @RequestParam(name = "noCategory", defaultValue = "false") boolean noCategory,
                                         Model model,
                                         Principal principal) {
        Integer authId = userService.getUserByEmail(principal.getName()).getId();
        List<Post> postList = feedService.getNewsFeedWithFilters(
                authId,
                isFriends,
                isSubscriptions,
                noCategory,
                category
        );
        List<Integer> postIdList = postList.stream().map(Post::getId).toList();
        HashMap<Integer, User> postAuthorsMap = new HashMap<>();
        for (Post post: postList) {
            postAuthorsMap.put(post.getId(), userService.getUserById(post.getAuthorId()));
        }
        model.addAttribute("postList", postList);
        model.addAttribute("postImagesIdList", postService.getPostImagesIdByPostIdList(postIdList));
        model.addAttribute("authId", authId);
        model.addAttribute("postAuthorsMap", postAuthorsMap);
        return "newsFeed";
    }
}
