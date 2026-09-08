package com.benjamin.Vocabulary.services;

import com.benjamin.Vocabulary.dto.WordDTO;
import com.benjamin.Vocabulary.entity.UserWord;
import com.benjamin.Vocabulary.entity.Word;
import com.benjamin.Vocabulary.repository.UserWordsRepository;
import com.benjamin.Vocabulary.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {

    @Mock
    private WordRepository wordRepository;

    @Mock
    private UserWordsRepository userWordsRepository;

    @InjectMocks
    private WordService wordService;

    @Test
    void shouldEnrichWordsInOriginalOrderWithOneBulkLookup() {
        UUID userId = UUID.randomUUID();
        Word firstWord = word(1L);
        Word secondWord = word(2L);
        UserWord firstUserWord = userWord(firstWord, 1);
        UserWord secondUserWord = userWord(secondWord, 3);

        when(wordRepository.findPaginatedWords(2, 0))
                .thenReturn(List.of(firstWord, secondWord));
        when(userWordsRepository.findByUserIdAndWordIdIn(userId, List.of(1L, 2L)))
                .thenReturn(List.of(secondUserWord, firstUserWord));

        List<WordDTO> result = wordService.getPaginatedWords(userId, 2, 0);

        assertEquals(List.of(firstWord, secondWord), result.stream().map(WordDTO::word).toList());
        assertEquals(List.of(1, 3), result.stream().map(WordDTO::note).toList());
        verify(userWordsRepository).findByUserIdAndWordIdIn(userId, List.of(1L, 2L));
    }

    private Word word(Long id) {
        Word word = new Word();
        word.setId(id);
        return word;
    }

    private UserWord userWord(Word word, int score) {
        UserWord userWord = new UserWord();
        userWord.setWord(word);
        userWord.setScore(score);
        return userWord;
    }
}