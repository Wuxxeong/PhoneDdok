package com.kindmz.smartphone.service;

import com.kindmz.smartphone.domain.Member;
import com.kindmz.smartphone.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public void createMember(String identity, String nickname) {
        Member newMember = new Member();
        newMember.setIdentity(identity);
        newMember.setNickname(nickname);

        memberRepository.save(newMember);
    }

    public Member getMemberByIdentity(String identity){ return memberRepository.findByIdentity(identity);    }
    public Member getMemberByIndex(Long index){ return memberRepository.findById(index).orElse(null); }

    public Member levelUpMemberByIndex(Long index, Integer upLevel){
        Member member = getMemberByIndex(index);
        if (member != null) { member.setLevel(member.getLevel() + upLevel); }
        return member;
    }

    public Member deleteMemberByIndex(Long index){ return clearMember(getMemberByIndex(index)); }
    public Member deleteMemberByIdentity(String identity){ return clearMember(getMemberByIdentity(identity)); }


    public Member clearMember(Member member){
        if (member == null) return null;

        member.setIdentity(null);
        member.setNickname(null);

        member.setAction("삭제됨");
        member.setDeletedTime(LocalDateTime.now());
        return member;
    }


}
