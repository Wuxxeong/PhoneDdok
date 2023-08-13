package com.kindmz.smartphone.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MemberDTO {

    private Long id;
    private String identity;
    private String nickname;
    private Integer level;
    private List<Long> favorites;
    private List<Long> finished;
    private LocalDateTime createdTime;
    private LocalDateTime deletedTime;
    private String action;
}
