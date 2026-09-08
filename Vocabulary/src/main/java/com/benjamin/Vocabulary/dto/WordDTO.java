package com.benjamin.Vocabulary.dto;

import com.benjamin.Vocabulary.entity.Word;

public record WordDTO(
    Word word,
    Integer note
) {
    
}
