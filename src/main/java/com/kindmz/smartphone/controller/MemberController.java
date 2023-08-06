package com.kindmz.smartphone.controller;

import com.kindmz.smartphone.domain.Member;
import com.kindmz.smartphone.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/new")
    private ResponseEntity<?> createMember(@RequestBody Member member){
        Member newMember = memberService.createMember(member.getIdentity(), member.getNickname());
        return newMember != null ?
                new ResponseEntity<>(newMember, HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    private ResponseEntity<?> getAllMembers(){
        List<Member> members = memberService.getAllMembers();
        return members.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/index/{index}")
    private ResponseEntity<?> getMemberByIndex(@PathVariable Long index){
        return memberExistResponse(memberService.getMemberByIndex(index));
    }
    // memberExistResponse는 해당 멤버가 존재하는지 확인하는 메소드.
    // 멤버를 찾아서 반환하는 형태는 반복 사용되므로, 하단에 따로 분리하여 생성했음

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
