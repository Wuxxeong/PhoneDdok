package com.kindmz.smartphone.service;

import com.kindmz.smartphone.domain.Member;
import com.kindmz.smartphone.dto.MemberDTO;
import com.kindmz.smartphone.mapper.MemberMapper;
import com.kindmz.smartphone.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

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
        member = memberRepository.save(member);
        return memberMapper.toDTO(member);
    }
    public MemberDTO addFavorites(MemberDTO memberDTO, List<Long> features) {
        Member member = memberMapper.toEntity(memberDTO);
        member.getFavorites().addAll(features);
        member = memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

    public MemberDTO addFinished(MemberDTO memberDTO, List<Long> features) {
        Member member = memberMapper.toEntity(memberDTO);
        member.getFinished().addAll(features);
        member = memberRepository.save(member);
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
        Member member = memberRepository.findById(memberDTO.getId()).orElse(null);

        if (member == null) {
            return null; // 또는 예외 처리를 수행
        }

        member.setLevel(member.getLevel() + upLevel);
        member = memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

//DELETE------------------------------------------------------------------------------
    public MemberDTO deleteFavorites(MemberDTO memberDTO, List<Long> features) {
        Member member = memberMapper.toEntity(memberDTO);
        member.getFavorites().removeAll(features);
        member = memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

    public MemberDTO deleteFinished(MemberDTO memberDTO, List<Long> features) {
        Member member = memberMapper.toEntity(memberDTO);
        member.getFinished().removeAll(features);
        member = memberRepository.save(member);
        return memberMapper.toDTO(member);
    }


    public MemberDTO deleteMemberByMember(MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberDTO.getId()).orElse(null);

        if (member == null) {
            return null; // 또는 예외 처리를 수행
        }

        member = clearMember(member);
        return memberMapper.toDTO(member);
    }


    public Member clearMember(Member member){
        if (member == null) return null;

        member.setIdentity(null);
        member.setNickname(null);

        member.setAction("삭제됨");
        member.setDeletedTime(LocalDateTime.now());
        memberRepository.save(member);
        return member;
    }


}
