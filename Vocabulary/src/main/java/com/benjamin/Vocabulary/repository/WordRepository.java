package com.benjamin.Vocabulary.repository;

import com.benjamin.Vocabulary.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = "SELECT * FROM words ORDER BY id ASC LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Word> findPaginatedWords(int limit, long cursor);

    @Query(value = "SELECT * FROM words WHERE note = ?1 ORDER BY id ASC LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Word> findPaginatedWordsByNote(Integer note, int limit, long cursor);

    @Query(value = "SELECT * FROM words ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Word findRandomWord();
}
