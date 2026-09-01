package com.benjamin.Vocabulary.controller;

import com.benjamin.Vocabulary.entity.Word;
import com.benjamin.Vocabulary.services.WordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam(defaultValue = "0") long cursor,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") Integer note
    ) {
        System.out.println("Received request with cursor: " + cursor + ", limit: " + limit + ", note: " + note);

        if (note != null && note > 0 && note <= 3) {
            return wordService.getPaginatedWordsByNote(note, limit, cursor * limit);
        }

        return wordService.getPaginatedWords(limit, cursor * limit);
    }
}
