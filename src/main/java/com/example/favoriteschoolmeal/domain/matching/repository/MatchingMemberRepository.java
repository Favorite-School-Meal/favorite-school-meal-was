package com.example.favoriteschoolmeal.domain.matching.repository;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.domain.MatchingMember;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.MatchingRequestStatus;
import java.util.List;
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

    /**
     * 특정 Matching에 대한 모든 MatchingMember를 조회합니다.
     *
     * @param matching 조회할 Matching 엔티티
     * @return 해당 Matching에 연관된 MatchingMember 리스트
     */
    List<MatchingMember> findAllByMatching(Matching matching);

    /**
     * 특정 Matching에 대한 특정 MatchingRequestStatus를 가진 MatchingMember를 조회합니다.
     *
     * @param matching 조회할 Matching 엔티티
     * @param status   조회할 MatchingRequestStatus
     * @return 해당 Matching에 연관된 MatchingMember 리스트
     */
    List<MatchingMember> findByMatchingAndMatchingRequestStatus(Matching matching,
            MatchingRequestStatus status);

    /**
     * 주어진 멤버와 관련된 모든 매칭 멤버를 조회합니다.
     *
     * @param member 조회할 멤버 엔티티
     * @return 해당 멤버에 연관된 MatchingMember 리스트
     */
    List<MatchingMember> findAllByMember(Member member);

    void deleteByMemberId(Long memberId);

    void deleteByPostId(Long id);
}