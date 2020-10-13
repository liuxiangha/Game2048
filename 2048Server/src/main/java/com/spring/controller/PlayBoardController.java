package com.spring.controller;

import com.spring.entity.PlayBoard;
import com.spring.repository.PlayBoardRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping(path = "/playBoard")
public class PlayBoardController {
    private final PlayBoardRepository playBoardRepository;

    public PlayBoardController(PlayBoardRepository playBoardRepository) {
        this.playBoardRepository = playBoardRepository;
    }

    @PostMapping(path = "/addNewBoardRecord")
    public @ResponseBody String addNewBoardRecord(@RequestParam Long userId, @RequestParam int takeTime,
                                                  @RequestParam Date playTime, @RequestParam String initialBoard) {
        try {
            PlayBoard playBoard = new PlayBoard();
            playBoard.setUserId(userId);
            playBoard.setTakeTime(takeTime);
            playBoard.setPlayTime(playTime);
            playBoard.setInitialBoard(initialBoard);

            playBoardRepository.save(playBoard);

            return "true";
        } catch (Exception e) {
            return "false";
        }
    }

    @GetMapping(path = "/getMinTakeTimeById")
    public String getMinTakeTimeById(@RequestParam Long userId){
        try {
            PlayBoard playBoard = playBoardRepository.
                    getPlay2048sByUserIdOrderByScore(userId).get(0);
            return String.valueOf(playBoard.getTakeTime());
        } catch (Exception e) {
            return "null";
        }
    }
}
