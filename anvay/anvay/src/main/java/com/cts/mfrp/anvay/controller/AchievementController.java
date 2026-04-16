package com.cts.mfrp.anvay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    @GetMapping("/{achievementId}")
    public ResponseEntity<Void> getAchievement(@PathVariable Integer achievementId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Void> getUserAchievements(@PathVariable Integer userId) {
        return ResponseEntity.ok().build();
    }
}
