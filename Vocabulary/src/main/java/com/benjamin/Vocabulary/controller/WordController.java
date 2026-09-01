package com.benjamin.Vocabulary.controller;

import com.benjamin.Vocabulary.entity.Word;
import com.benjamin.Vocabulary.services.WordService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/words")
@AllArgsConstructor
@CrossOrigin(origins = "*") // Allow requests from frontend
public class WordController {

    private WordService wordService;

    @GetMapping("/random")
    public ResponseEntity<Word> getRandomWord() {
        return ResponseEntity.ok(wordService.getRandomWord());
    }

    @PutMapping("/{id}/note")
    public ResponseEntity<Word> updateNote(@PathVariable Long id, @RequestBody Integer newNote) {
        return ResponseEntity.ok(wordService.updateNote(id, newNote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Word> updateWord(@PathVariable Long id, @RequestBody Word wordDetails) {
        return ResponseEntity.ok(wordService.updateWord(id, wordDetails));
    }
}
