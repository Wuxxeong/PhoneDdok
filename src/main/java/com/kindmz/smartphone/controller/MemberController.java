package com.kindmz.smartphone.controller;

import com.kindmz.smartphone.domain.Member;
import com.kindmz.smartphone.service.MemberService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;
//POST------------------------------------------------------------------------------
    @PostMapping // 멤버 생성
    public ResponseEntity<?> createMember(@RequestBody Member member) {
        if (member.getIdentity() == null || member.getNickname() == null)
            return new ResponseEntity<>("아이디/닉네임 입력되지 않음",HttpStatus.BAD_REQUEST);

        boolean isMemberExist =
                memberService.getMemberByIdentity(member.getIdentity()) != null ||
                memberService.getMemberByNickname(member.getNickname()) != null;

        return isMemberExist ?
                new ResponseEntity<>("이미 존재하는 멤버", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(memberService.createMember(member), HttpStatus.CREATED);
    }

    @PostMapping("/favorites") // 즐겨찾기 추가
    public ResponseEntity<?> addFavorites(@RequestParam String identity, @RequestParam List<Long> features) {
        Member member = memberService.getMemberByIdentity(identity);
        if (member == null){ return new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND); }

        memberService.addFavorites(member, features);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PostMapping("/finish") // 공부한 기능 추가
    public ResponseEntity<?> addFinished(@RequestParam String identity, @RequestParam List<Long> features) {
        Member member = memberService.getMemberByIdentity(identity);
        if (member == null){ return new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND); }

        memberService.addFinished(member, features);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

//GET------------------------------------------------------------------------------
    @GetMapping // 멤버 조회 (전체 / 특정)
    public ResponseEntity<?> getMembers(@RequestParam(value = "identity", required = false) String identity,
                                        @RequestParam(value = "nickname", required = false) String nickname)
    {
        if (identity != null && nickname != null)
            return new ResponseEntity<>("아이디와 닉네임 중 하나만 입력해주세요.", HttpStatus.BAD_REQUEST);
        if (identity == null && nickname == null)
            return new ResponseEntity<>(memberService.getAllMembers(), HttpStatus.OK); // 모든 계정 조회

        Member member = memberService.getMemberByInfo(identity, nickname);
        return member != null ? new ResponseEntity<>(member, HttpStatus.OK) : new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/existence") // 멤버 존재 여부 조회
    public ResponseEntity<?> isMemberExist(@RequestParam(value = "identity", required = false) String identity,
                                           @RequestParam(value = "nickname", required = false) String nickname)
    {
        if (identity == null && nickname == null)
            return new ResponseEntity<>("계정을 조회할 요소(아이디/닉네임)가 입력되지 않음", HttpStatus.BAD_REQUEST);

        Map<String, Boolean> response = new HashMap<>();
        if (identity != null)
            response.put("identityExist", memberService.getMemberByIdentity(identity) != null);
        if (nickname != null)
            response.put("nicknameExist", memberService.getMemberByNickname(nickname) != null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/favorites") // 즐겨찾기 조회 - List<Long> 반환
    public ResponseEntity<?> getFavorites(@RequestParam String identity) {
        Member member = memberService.getMemberByIdentity(identity);
        if (member == null){ return new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND); }

        return new ResponseEntity<>(memberService.getFavorites(member), HttpStatus.OK);
    }

    @GetMapping("/finish") // 즐겨찾기 조회 - List<Long> 반환
    public ResponseEntity<?> getFinished(@RequestParam String identity) {
        Member member = memberService.getMemberByIdentity(identity);
        if (member == null){ return new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND); }

        return new ResponseEntity<>(memberService.getFinished(member), HttpStatus.OK);
    }


//PUT------------------------------------------------------------------------------
    @PutMapping // 멤버 업데이트
    public ResponseEntity<?> updateMember(@RequestBody UpdateBody updateBody) {
        String identity = updateBody.getIdentity();
        String nickname = updateBody.getNickname();

        if (identity != null && nickname != null)
            return new ResponseEntity<>("아이디와 닉네임 중 하나만 입력해주세요.", HttpStatus.BAD_REQUEST);
        if (identity == null && nickname == null)
            return new ResponseEntity<>("계정을 조회할 요소(아이디/닉네임)가 입력되지 않음", HttpStatus.BAD_REQUEST);

        Member member = memberService.getMemberByInfo(identity, nickname);
        if (member == null) {return new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND);}

        // TODO: updateBody에 내용을 추가하여, 각 내용당 처리할 내용을 처리해주어야 함.
        if (updateBody.getLevelUp() != 0){
            memberService.levelUpByMember(member, updateBody.getLevelUp());
        }

        return new ResponseEntity<>(member, HttpStatus.OK);
    }


//DELETE------------------------------------------------------------------------------
    @DeleteMapping // 멤버 삭제(초기화)
    public ResponseEntity<?> deleteMember(@RequestParam(value = "identity", required = false) String identity,
                                          @RequestParam(value = "nickname", required = false) String nickname)
    {
        if (identity != null && nickname != null)
            return new ResponseEntity<>("아이디와 닉네임 중 하나만 입력해주세요.", HttpStatus.BAD_REQUEST);
        if (identity == null && nickname == null)
            return new ResponseEntity<>("계정을 조회할 요소(아이디/닉네임)가 입력되지 않음", HttpStatus.BAD_REQUEST);

        Member member = memberService.getMemberByInfo(identity, nickname);
        if (member == null) {return new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND);}

        return new ResponseEntity<>(memberService.deleteMemberByMember(member), HttpStatus.OK);
    }

    @DeleteMapping("/favorites") // 즐겨찾기 삭제
    public ResponseEntity<?> deleteFavorites(@RequestParam String identity, @RequestParam List<Long> features) {
        Member member = memberService.getMemberByIdentity(identity);
        if (member == null){ return new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND); }

        memberService.deleteFavorites(member, features);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @DeleteMapping("/finish") // 공부한 기능 삭제
    public ResponseEntity<?> deleteFinished(@RequestParam String identity, @RequestParam List<Long> features) {
        Member member = memberService.getMemberByIdentity(identity);
        if (member == null){ return new ResponseEntity<>("멤버가 발견되지 않음",HttpStatus.NOT_FOUND); }

        memberService.deleteFinished(member, features);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }


//------------------------------------------------------------------------------
    @Getter @Setter
    public static class UpdateBody {
        private String identity = null;
        private String nickname = null;
        private int levelUp = 0;
    }
}
