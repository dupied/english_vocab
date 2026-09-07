package com.benjamin.Vocabulary.repository;

import com.benjamin.Vocabulary.entity.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserWordsRepository extends JpaRepository<UserWord, Long> {
    Optional<UserWord> findByUserIdAndWordId(UUID userId, Long wordId);
}