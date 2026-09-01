package com.benjamin.Vocabulary.controller;

import com.benjamin.Vocabulary.entity.Word;
import com.benjamin.Vocabulary.services.WordService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WordPaginationControllerTest {

    // @Test
    // void shouldReturnPaginatedWordsFilteredByNote() throws Exception {
    //     WordService wordService = Mockito.mock(WordService.class);
    //     WordPaginationController controller = new WordPaginationController(wordService);
    //     MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    //     Word word = new Word();
    //     Mockito.when(wordService.getPaginatedWordsByNote(eq(2), eq(100), eq(0L))).thenReturn(List.of(word));

    //     mockMvc.perform(get("/api/words")
    //                     .param("cursor", "0")
    //                     .param("limit", "100")
    //                     .param("note", "2"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$[0].wordEn").value("apple"))
    //             .andExpect(jsonPath("$[0].note").value(2));
    // }

    // @Test
    // void shouldReturnAllWordsWhenNoteIsZero() throws Exception {
    //     WordService wordService = Mockito.mock(WordService.class);
    //     WordPaginationController controller = new WordPaginationController(wordService);
    //     MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    //     Word word = new Word();
    //     Mockito.when(wordService.getPaginatedWords(eq(100), eq(0L))).thenReturn(List.of(word));

    //     mockMvc.perform(get("/api/words")
    //                     .param("cursor", "0")
    //                     .param("limit", "100")
    //                     .param("note", "0"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$[0].wordEn").value("banana"));
    // }
}
