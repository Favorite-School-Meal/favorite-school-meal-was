package com.example.favoriteschoolmeal.global.security.util;

import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.security.userdetails.CustomUserDetails;
import java.util.function.Supplier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 애플리케이션 내 보안 관련 작업을 위한 유틸리티 메서드를 제공합니다.
 */
public class SecurityUtils {

    /**
     * 현재 인증된 사용자가 관리자 권한을 가지고 있는지 확인합니다.
     *
     * @return 사용자가 관리자 권한을 가지고 있다면 true, 그렇지 않다면 false를 반환합니다.
     */
    public static boolean isAdmin() {
        return hasAuthority(Authority.ROLE_ADMIN.name());
    }

    /**
     * 현재 인증된 사용자가 일반 사용자 또는 관리자 권한 중 하나를 가지고 있는지 확인합니다.
     *
     * @return 사용자가 일반 사용자 또는 관리자 권한을 가지고 있다면 true, 그렇지 않다면 false를 반환합니다.
     */
    public static boolean isUserOrAdmin() {
        return hasAuthority(Authority.ROLE_USER.name()) || isAdmin();
    }

    /**
     * 현재 인증된 사용자가 관리자 권한을 가지고 있지 않다면 예외를 발생시킵니다.
     *
     * @param exceptionSupplier 예외를 제공하는 공급자입니다.
     */
    public static void checkAdminOrThrow(Supplier<? extends BaseException> exceptionSupplier) {
        throwIf(!isAdmin(), exceptionSupplier);
    }

    /**
     * 현재 인증된 사용자가 일반 사용자 또는 관리자 권한을 가지고 있지 않다면 예외를 발생시킵니다.
     *
     * @param exceptionSupplier 예외를 제공하는 공급자입니다.
     */
    public static void checkUserOrAdminOrThrow(
            Supplier<? extends BaseException> exceptionSupplier) {
        throwIf(!isUserOrAdmin(), exceptionSupplier);
    }

    /**
     * 현재 인증된 사용자의 ID를 검색합니다.
     *
     * @param exceptionSupplier 사용자 정보를 찾을 수 없는 경우 발생시킬 예외를 제공하는 공급자입니다.
     * @return 현재 인증된 사용자의 사용자 ID입니다.
     */
    public static Long getCurrentMemberId(Supplier<? extends BaseException> exceptionSupplier) {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        }
        throw exceptionSupplier.get();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private static <E extends BaseException> void throwIf(boolean condition,
            Supplier<E> exceptionSupplier) {
        if (condition) {
            throw exceptionSupplier.get();
        }
    }

    private static boolean hasAuthority(String authority) {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }
}
