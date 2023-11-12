package com.example.favoriteschoolmeal.domain.chat.domain;

import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Chat extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", updatable = false)
    private Long id;


}
