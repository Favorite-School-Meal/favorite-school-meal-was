package com.example.favoriteschoolmeal.domain.friend.repository;

import com.example.favoriteschoolmeal.domain.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
