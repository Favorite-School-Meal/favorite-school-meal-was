package com.example.favoriteschoolmeal.domain.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.service.MatchingService;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.domain.model.Gender;
import com.example.favoriteschoolmeal.domain.model.GroupState;
import com.example.favoriteschoolmeal.domain.model.Location;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.domain.post.service.dto.CreatePostCommand;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MatchingService matchingService;

    @Test
    @DisplayName("게시글 추가 테스트")
    void addPostTest() {
        // 가짜 Member와 Matching 객체 생성
        Member mockMember = Member.builder()
                .username("testUser")
                .nickname("Test")
                .fullName("Test User")
                .password("password")
                .email("test@example.com")
                .authority(Authority.ROLE_USER)
                .isBanned(false)
                .age(25)
                .gender(Gender.MALE)
                .introduction("Test Introduction")
                .build();

        Matching mockMatching = Matching.builder()
                .groupState(GroupState.IN_PROGRESS)
                .maxParticipant(5L)
                .location(new Location())
                .build();

        // MemberService와 MatchingService의 메서드가 호출될 때 모의 객체 반환
        when(memberService.findMemberById(anyLong())).thenReturn(Optional.of(mockMember));
        when(matchingService.addMatching()).thenReturn(mockMatching);

        // 테스트할 CreatePostCommand 객체 생성
        CreatePostCommand command = new CreatePostCommand(1L, null, "Test Title", "Test Content");

        // PostRepository의 save 메서드가 호출될 때 Post 객체 반환
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 메서드 호출 및 검증
        Post result = postService.addPost(command);
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals(mockMember, result.getMember());
        assertEquals(mockMatching, result.getMatching());
    }
}