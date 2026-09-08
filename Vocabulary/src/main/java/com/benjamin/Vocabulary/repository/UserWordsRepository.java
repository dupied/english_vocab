package com.benjamin.Vocabulary.repository;

import com.benjamin.Vocabulary.entity.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserWordsRepository extends JpaRepository<UserWord, Long> {
    Optional<UserWord> findByUserIdAndWordId(UUID userId, Long wordId);

    @Query(value = "SELECT * FROM user_words WHERE user_id = :userId AND word_id IN (:wordIds)", nativeQuery = true)
    List<UserWord> findByUserIdAndWordIdIn(@Param("userId") UUID userId, @Param("wordIds") List<Long> wordIds);
}