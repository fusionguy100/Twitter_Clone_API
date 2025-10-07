package com.cooksys.social_media.controllers;

import com.cooksys.social_media.services.ValidateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
    private ValidateService validateService;

    @GetMapping("/tag/exists/{label}")
    public ResponseEntity<Boolean> validateTagExists(@PathVariable String label) {
        return ResponseEntity.ok(validateService.validateTagExists(label));

    }

    @GetMapping("/username/exists/@{username}")
    public ResponseEntity<Boolean> validateUsernameExists(@PathVariable String username) {
        return ResponseEntity.ok(validateService.validateUsernameExists(username));
    }

    @GetMapping("/username/available/@{username}")
    public ResponseEntity<Boolean> validateUsernameAvailable(@PathVariable String username) {
        return ResponseEntity.ok(validateService.validateUsernameAvailable(username));
    }
}
