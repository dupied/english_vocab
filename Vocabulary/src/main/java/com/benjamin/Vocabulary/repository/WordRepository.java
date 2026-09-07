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

    @Query(value = "SELECT w.* FROM words w LEFT JOIN user_words uw ON w.id = uw.word_id AND uw.user_id = ?1 WHERE uw.score IS NULL OR uw.score = 1 ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Word findRandomWordForUser(java.util.UUID userId);

    @Query(value = "SELECT w.* FROM words w LEFT JOIN user_words uw ON w.id = uw.word_id AND uw.user_id = ?1 WHERE uw.score = ?2 ORDER BY w.id ASC LIMIT ?3 OFFSET ?4", nativeQuery = true)
    List<Word> findPaginatedWordsByNoteForUser(java.util.UUID userId, Integer note, int limit, long cursor);
}
