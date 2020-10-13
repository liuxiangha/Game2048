package com.spring.controller;

import com.spring.entity.PlayUser;
import com.spring.repository.PlayUserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/playUser")
public class PlayUserController {
    private final PlayUserRepository playUserRepository;

    public PlayUserController(PlayUserRepository playUserRepository) {
        this.playUserRepository = playUserRepository;
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam Long userId, @RequestParam String userName,
                                           @RequestParam String userPassword, @RequestParam String userGender){
        PlayUser obj = playUserRepository.getPlayUserByUserId(userId);
        if (obj != null) return "No";

        try {
            PlayUser playUser = new PlayUser();
            playUser.setUserId(userId);
            playUser.setUserName(userName);
            playUser.setUserPassword(userPassword);
            playUser.setUserGender(userGender);
            playUserRepository.save(playUser);
            return "true";
        } catch (Exception e) {
            return "false";
        }
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<PlayUser> getAllPlayUser() {
        return playUserRepository.findAll();
    }

    @PostMapping(path = "/checkPlayUser")
    public @ResponseBody String checkPlayUser(@RequestParam Long playUserId,
                                              @RequestParam String playUserPassword) {
        PlayUser playUser = playUserRepository.
                getPlayUserByUserIdAndUserPassword(playUserId, playUserPassword);

        return playUser != null? "true":"false";
    }

    @GetMapping(path = "/getUserNameById")
    public @ResponseBody String getUserNameById(@RequestParam Long userId) {
        return playUserRepository.getPlayUserNameByUserId(userId).getUserName();
    }
}
