package com.benjamin.Vocabulary.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.benjamin.Vocabulary.entity.Word;
import com.benjamin.Vocabulary.repository.WordRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WordService {
    
    private WordRepository wordRepository;

    public Word getRandomWord() {
        return wordRepository.findRandomWord();
    }

    public Word updateNote(Long id, Integer newNote) {
        return null;
        // return wordRepository.findById(id)
        //         .map(word -> {
        //             word.setNote(newNote);
        //             return wordRepository.save(word);
        //         })
        //         .orElse(null);
    }

    public Word updateWord(Long id, Word wordDetails) {
        return wordRepository.findById(id)
                .map(word -> {
                    word.setWordEn(wordDetails.getWordEn());
                    word.setWordFr(wordDetails.getWordFr());
                    word.setExampleEn(wordDetails.getExampleEn());
                    return wordRepository.save(word);
                })
                .orElse(null);
    }

    public List<Word> getPaginatedWords(int limit, long cursor) {
        return wordRepository.findPaginatedWords(limit, cursor);
    }

    public List<Word> getPaginatedWordsByNote(Integer note, int limit, long cursor) {
        return wordRepository.findPaginatedWordsByNote(note, limit, cursor);
    }
    
}
