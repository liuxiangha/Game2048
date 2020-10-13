package com.spring.controller;

import com.spring.entity.Play2048;
import com.spring.repository.Play2048Repository;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping(path = "/play2048")
public class Play2048Controller {
    private final Play2048Repository play2048Repository;

    public Play2048Controller(Play2048Repository play2048Repository) {
        this.play2048Repository = play2048Repository;
    }

    @PostMapping(path = "/addNewRecord")
    public @ResponseBody String addNewPlayRecord(@RequestParam Long userId, @RequestParam int score,
                                                 @RequestParam Date playTime, @RequestParam String finialResult) {
        try {
            Play2048 play2048 = new Play2048();
            play2048.setUserId(userId);
            play2048.setScore(score);
            play2048.setPlayTime(playTime);
            play2048.setFinialResult(finialResult);
            play2048Repository.save(play2048);
            return "true";
        } catch (Exception e) {
            return "false";
        }
    }

    @GetMapping(path = "/getMaxScoreById")
    public @ResponseBody String getMaxScoreById(@RequestParam Long userId) {
        try {
            Play2048 play2048 = play2048Repository.
                    getPlay2048sByUserIdOrderByScore(userId).get(0);
            return String.valueOf(play2048.getScore());
        }catch (Exception e) {
            return "null";
        }
    }
}
