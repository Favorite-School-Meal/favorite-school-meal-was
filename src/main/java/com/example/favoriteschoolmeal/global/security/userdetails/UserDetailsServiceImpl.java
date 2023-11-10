package com.example.favoriteschoolmeal.global.security.userdetails;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

   //사용자 정보를 db 또는 외부 시스템에서 가져와 UserDetails 객체로 변환하는 로직
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username);

        if(member != null){
            return new CustomUserDetails(member);
        } else{
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다. " + username);
        }
    }
}
