package com.example.favoriteschoolmeal.global.security.util;

import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.security.userdetails.CustomUserDetails;
import java.util.function.Supplier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security 관련 유틸 클래스
 * <p>
 * 사용 예시
 * 1. 현재 사용자 ID를 얻는데 실패한 경우 PostException을 던짐
 * Long memberId = SecurityUtils.getCurrentMemberId(() -> new PostException(PostExceptionType.MEMBER_NOT_FOUND));
 * <p>
 * 2. 권한이 없는 경우 PostException을 던짐
 * SecurityUtils.checkUserAuthority("ROLE_USER", () -> new PostException(PostExceptionType.UNAUTHORIZED_ACCESS));
 */
public class SecurityUtils {

    public static <E extends BaseException> void throwIf(boolean condition,
            Supplier<E> exceptionSupplier) {
        if (condition) {
            throw exceptionSupplier.get();
        }
    }

    public static Long getCurrentMemberId(Supplier<? extends BaseException> exceptionSupplier) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        }
        throw exceptionSupplier.get();
    }

    public static void checkUserAuthority(String role,
            Supplier<? extends BaseException> exceptionSupplier) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isUnauthorized = authentication == null || !authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(role));
        throwIf(isUnauthorized, exceptionSupplier);
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(Authority.ROLE_ADMIN.name()));
    }
}
