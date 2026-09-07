package com.benjamin.Vocabulary.services;

import com.benjamin.Vocabulary.entity.UserWord;
import com.benjamin.Vocabulary.entity.Word;
import com.benjamin.Vocabulary.repository.UserWordsRepository;
import com.benjamin.Vocabulary.repository.WordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final UserWordsRepository userWordsRepository;

    public Word getRandomWord(UUID userId) {
        return wordRepository.findRandomWordForUser(userId);
    }

    public Word updateNote(UUID userId, Long wordId, Integer newNote) {
        if (newNote == null || newNote < 1 || newNote > 3) {
            throw new IllegalArgumentException("Score must be between 1 and 3");
        }

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new EntityNotFoundException("Word not found with id: " + wordId));

        UserWord userWord = userWordsRepository.findByUserIdAndWordId(userId, wordId)
                .orElseGet(() -> {
                    UserWord newUserWord = new UserWord();
                    newUserWord.setUserId(userId);
                    newUserWord.setWord(word);
                    return newUserWord;
                });

        userWord.setScore(newNote);
        userWordsRepository.save(userWord);
        return word;
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

    public List<Word> getPaginatedWordsByNote(UUID userId, Integer note, int limit, long cursor) {
        if (note == null || note < 1 || note > 3) {
            return getPaginatedWords(limit, cursor);
        }
        return wordRepository.findPaginatedWordsByNoteForUser(userId, note, limit, cursor);
    }
}
