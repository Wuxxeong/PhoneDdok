package com.kindmz.smartphone.mapper;

import com.kindmz.smartphone.domain.Member;
import com.kindmz.smartphone.dto.MemberDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member toEntity(MemberDTO memberDTO);

    MemberDTO toDTO(Member member);

    List<MemberDTO> toDTOList(List<Member> allMembers);
}