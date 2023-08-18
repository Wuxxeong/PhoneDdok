package com.kindmz.smartphone.service;

import com.kindmz.smartphone.domain.Member;
import com.kindmz.smartphone.dto.MemberDTO;
import com.kindmz.smartphone.mapper.MemberMapper;
import com.kindmz.smartphone.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequestMapping("/members")
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
//CREATE------------------------------------------------------------------------------
    public MemberDTO createMember(MemberDTO memberDTO) {
        Member member = memberMapper.toEntity(memberDTO);
        member.setLevel(1);
        member.setCreatedTime(LocalDateTime.now());
        member.setAction("생성됨");
        memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

    public MemberDTO addFavorites(MemberDTO memberDTO, List<Long> features) {
        Member member = memberMapper.toEntity(memberDTO);

        Set<Long> favoritesSet = new HashSet<>(member.getFavorites()); // 기존 즐겨찾기 목록을 Set으로 변환
        favoritesSet.addAll(features);
        member.setFavorites(new ArrayList<>(favoritesSet));

        memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

    public MemberDTO addFinished(MemberDTO memberDTO, List<Long> features) {
        Member member = memberMapper.toEntity(memberDTO);

        Set<Long> finishedSet = new HashSet<>(member.getFinished()); // 기존 즐겨찾기 목록을 Set으로 변환
        finishedSet.addAll(features);
        member.setFavorites(new ArrayList<>(finishedSet));

        memberRepository.save(member);
        return memberMapper.toDTO(member);
    }


//READ------------------------------------------------------------------------------
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        return member != null ? memberMapper.toDTO(member) : null;
    }
    public MemberDTO getMemberByIdentity(String identity) {
        Member member = memberRepository.findByIdentity(identity);
        return memberMapper.toDTO(member);
    }

    public MemberDTO getMemberByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname);
        return memberMapper.toDTO(member);
    }
    public List<MemberDTO> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return memberMapper.toDTOList(members);
    }
    public MemberDTO getMemberByInfo(String identity, String nickname) {
        Member member = identity != null ? memberRepository.findByIdentity(identity) : memberRepository.findByNickname(nickname);
        return memberMapper.toDTO(member);
    }

    public List<Long> getFavorites(MemberDTO memberDTO) {
        Member member = memberMapper.toEntity(memberDTO);
        return member.getFavorites();
    }

    public List<Long> getFinished(MemberDTO memberDTO) {
        Member member = memberMapper.toEntity(memberDTO);
        return member.getFinished();
    }



//UPDATE------------------------------------------------------------------------------
    public MemberDTO levelUpByMember(MemberDTO memberDTO, Integer upLevel) {
        Member member = memberMapper.toEntity(memberDTO);

        if (member == null) {
            return null; // 또는 예외 처리를 수행
        }

        member.setLevel(member.getLevel() + upLevel);
        memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

    public MemberDTO updatePhoneByMember(MemberDTO memberDTO, Integer phone) {
        Member member = memberMapper.toEntity(memberDTO);

        if (member == null) {
            return null; // 또는 예외 처리를 수행
        }

        member.setPhone(phone);
        memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

//DELETE------------------------------------------------------------------------------
    public MemberDTO deleteFavorites(MemberDTO memberDTO, List<Long> features) {
        Member member = memberMapper.toEntity(memberDTO);
        member.getFavorites().removeAll(features);
        memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

    public MemberDTO deleteFinished(MemberDTO memberDTO, List<Long> features) {
        Member member = memberMapper.toEntity(memberDTO);
        member.getFinished().removeAll(features);
        memberRepository.save(member);
        return memberMapper.toDTO(member);
    }


    public MemberDTO deleteMemberByMember(MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberDTO.getId()).orElse(null);

        if (member == null) {
            return null; // 또는 예외 처리를 수행
        }

        clearMember(member);
        return memberMapper.toDTO(member);
    }


    public void clearMember(Member member){
        if (member == null) return;

        member.setIdentity(null);
        member.setNickname(null);

        member.setAction("삭제됨");
        member.setDeletedTime(LocalDateTime.now());
        memberRepository.save(member);
    }


}
