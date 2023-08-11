package com.kindmz.smartphone.service;

import com.kindmz.smartphone.domain.Member;
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

    // CREATE 영역
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    // READ 영역
    public Member getMemberByIdentity(String identity) { return memberRepository.findByIdentity(identity); }
    public Member getMemberByNickname(String nickname) { return memberRepository.findByNickname(nickname); }
    public List<Member> getAllMembers(){ return memberRepository.findAll(); }
    public Member getMemberByInfo(String identity, String nickname){
        return identity != null ? memberRepository.findByIdentity(identity) : memberRepository.findByNickname(nickname);
    }

    // UPDATE 영역
    public void levelUpByMember(Member member, Integer upLevel){
//        Integer changedLevel = member.getLevel() + upLevel; // 그냥 바로 넣으면 반영이 안되길래 이렇게 수정해봤습니다.
        member.setLevel(member.getLevel() + upLevel);
        memberRepository.save(member);
    }

    // DELETE 영역
    public Member deleteMemberByMember(Member member){ return clearMember(member); }


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
