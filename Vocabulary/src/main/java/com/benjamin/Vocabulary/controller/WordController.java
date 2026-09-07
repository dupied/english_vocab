package com.benjamin.Vocabulary.controller;

import com.benjamin.Vocabulary.entity.Word;
import com.benjamin.Vocabulary.services.WordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/words")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class WordController {

    private final WordService wordService;

    @GetMapping("/random")
    public ResponseEntity<Word> getRandomWord(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(wordService.getRandomWord(userIdFrom(jwt)));
    }

    @PutMapping("/{id}/note")
    public ResponseEntity<Word> updateNote(@PathVariable Long id,
                                          @AuthenticationPrincipal Jwt jwt,
                                          @RequestBody Integer newNote) {
        return ResponseEntity.ok(wordService.updateNote(userIdFrom(jwt), id, newNote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Word> updateWord(@PathVariable Long id, @RequestBody Word wordDetails) {
        return ResponseEntity.ok(wordService.updateWord(id, wordDetails));
    }

    private UUID userIdFrom(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
