package com.kindmz.smartphone.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Long index;

    @Column(nullable = false)
    private String identity; // 아이디
    @Column(nullable = false)
    private String nickname; // 닉네임

    private Integer level = 1;

    private LocalDateTime createdTime = LocalDateTime.now();
    private LocalDateTime deletedTime = null;
    private String action = "생성됨";
}
