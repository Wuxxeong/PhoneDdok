package com.kindmz.smartphone.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String identity; // 아이디
    private String nickname; // 닉네임

    private Integer level = 1;

    @ElementCollection
    private List<Long> favorites = new ArrayList<>();
    @ElementCollection
    private List<Long> finished = new ArrayList<>();

    private LocalDateTime createdTime = LocalDateTime.now();
    private LocalDateTime deletedTime = null;
    private String action = "생성됨";
}
