package com.example.favoriteschoolmeal.domain.chat.service;

import com.example.favoriteschoolmeal.domain.chat.domain.Chat;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {

    public Optional<Chat> findChatOptionally(Long chatId) {
        //TODO: 구현 필요
        return Optional.empty();
    }
}
