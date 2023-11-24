package com.example.favoriteschoolmeal.domain.matching.repository;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.domain.MatchingMember;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.MatchingRequestStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingMemberRepository extends JpaRepository<MatchingMember, Long> {

    /**
     * 매칭과 멤버를 기반으로 매칭 멤버를 찾습니다.
     *
     * @param matching  매칭 엔티티
     * @param applicant 멤버 엔티티
     * @return 매칭 멤버
     */
    Optional<MatchingMember> findByMatchingAndMember(Matching matching, Member applicant);

    /**
     * 주어진 매칭과 매칭 요청 상태에 해당하는 매칭 멤버 수를 반환합니다.
     *
     * @param matching 매칭 엔티티
     * @param status   매칭 요청 상태
     * @return 해당 조건을 만족하는 매칭 멤버 수
     */
    long countByMatchingAndMatchingRequestStatus(Matching matching, MatchingRequestStatus status);
}