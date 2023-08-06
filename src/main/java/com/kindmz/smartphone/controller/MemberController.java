package com.kindmz.smartphone.controller;

import com.kindmz.smartphone.domain.Member;
import com.kindmz.smartphone.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/new")
    private void createMember(@RequestBody Member member){
        memberService.createMember(member.getIdentity(), member.getNickname());
    }

    @GetMapping("/index/{index}")
    private ResponseEntity<?> getMemberByIndex(@PathVariable Long index){
        return memberExistResponse(memberService.getMemberByIndex(index));
    }
    // 멤버를 찾아서 반환하는 형태는 반복되는 구문이므로, memeberExistResponse 메소드를 하단에 따로 분리하여 생성했음

    @GetMapping("/identity/{identity}")
    private ResponseEntity<?> getMemberByIdentity(@PathVariable String identity){
        return memberExistResponse(memberService.getMemberByIdentity(identity));
    }

    @PutMapping("/index/{index}/{upLevel}")
    private ResponseEntity<?> levelUpMember(@PathVariable Long index, @PathVariable Integer upLevel){
        return memberExistResponse(memberService.levelUpMemberByIndex(index, upLevel));
    }

    @DeleteMapping("/index/{index}")
    private ResponseEntity<?> deleteMemberByIndex(@PathVariable Long index){
        return memberExistResponse(memberService.deleteMemberByIndex(index));
    }

    @DeleteMapping("/identity/{identity}")
    private ResponseEntity<?> deleteMemberByIdentity(@PathVariable String identity){
        return memberExistResponse(memberService.deleteMemberByIdentity(identity));
    }

    private ResponseEntity<?> memberExistResponse(Member member){
        return member != null ?
                new ResponseEntity<Member>(member, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
