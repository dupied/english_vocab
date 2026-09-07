package com.benjamin.Vocabulary.controller;

import com.benjamin.Vocabulary.entity.Word;
import com.benjamin.Vocabulary.services.WordService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WordPaginationController {

    private final WordService wordService;

    public WordPaginationController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping(value = "/words", params = {"cursor", "limit"})
    public List<Word> getWords(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") long cursor,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") Integer note
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        if (note != null && note > 0 && note <= 3) {
            return wordService.getPaginatedWordsByNote(userId, note, limit, cursor * limit);
        }

        return wordService.getPaginatedWords(limit, cursor * limit);
    }
}
