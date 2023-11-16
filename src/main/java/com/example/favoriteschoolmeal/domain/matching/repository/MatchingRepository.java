package com.example.favoriteschoolmeal.domain.matching.repository;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

}
