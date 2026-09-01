package com.benjamin.Vocabulary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "words")
@Getter @Setter
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word_en", nullable = false)
    private String wordEn;

    @Column(name = "word_fr", nullable = false)
    private String wordFr;

    @Column(name = "example_en")
    private String exampleEn;
}
