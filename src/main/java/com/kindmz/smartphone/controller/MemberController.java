package com.kindmz.smartphone.controller;

import com.kindmz.smartphone.domain.Member;
import com.kindmz.smartphone.dto.MemberDTO;
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
    public ResponseEntity<?> createMember(@RequestBody MemberDTO memberDTO) {
        if (memberDTO.getIdentity() == null || memberDTO.getNickname() == null)
            return new ResponseEntity<>("아이디/닉네임 입력되지 않음", HttpStatus.BAD_REQUEST);

        boolean isMemberExist =
                memberService.getMemberByIdentity(memberDTO.getIdentity()) != null ||
                        memberService.getMemberByNickname(memberDTO.getNickname()) != null;

        if (isMemberExist) {
            return new ResponseEntity<>("이미 존재하는 멤버", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(memberService.createMember(memberDTO), HttpStatus.CREATED);
        }
    }

    @PostMapping("/favorites") // 즐겨찾기 추가
    public ResponseEntity<?> addFavorites(@RequestParam String identity, @RequestParam List<Long> features) {
        MemberDTO memberDTO = memberService.getMemberByIdentity(identity);
        if (memberDTO == null) {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }

        memberService.addFavorites(memberDTO, features);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    @PostMapping("/finish") // 공부한 기능 추가
    public ResponseEntity<?> addFinished(@RequestParam String identity, @RequestParam List<Long> features) {
        MemberDTO memberDTO = memberService.getMemberByIdentity(identity);
        if (memberDTO == null) {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }

        memberService.addFinished(memberDTO, features);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

//GET------------------------------------------------------------------------------
    @GetMapping // 멤버 조회 (전체 / 특정)
    public ResponseEntity<?> getMembers(@RequestParam(value = "identity", required = false) String identity,
                                    @RequestParam(value = "nickname", required = false) String nickname) {
        if (identity != null && nickname != null) {
            return new ResponseEntity<>("아이디와 닉네임 중 하나만 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        if (identity == null && nickname == null) {
            List<MemberDTO> memberDTOs = memberService.getAllMembers();
            return new ResponseEntity<>(memberDTOs, HttpStatus.OK); // 모든 계정 조회
        }

        MemberDTO memberDTO = memberService.getMemberByInfo(identity, nickname);
        if (memberDTO != null) {
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }
    }//바꿈
    @GetMapping("/{id}") // 멤버 조회 (인덱스 이용)
    public ResponseEntity<?> getMemberById(@PathVariable Long id) {
        MemberDTO memberDTO = memberService.getMemberById(id);
        if (memberDTO != null) {
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/existence") // 멤버 존재 여부 조회 (특정)
    public ResponseEntity<?> isMemberExist(@RequestParam(value = "identity", required = false) String identity,
                                           @RequestParam(value = "nickname", required = false) String nickname) {
        if (identity == null && nickname == null) {
            return new ResponseEntity<>("계정을 조회할 요소(아이디/닉네임)가 입력되지 않음", HttpStatus.BAD_REQUEST);
        }

        Map<String, Boolean> response = new HashMap<>();
        if (identity != null) {
            response.put("identityExist", memberService.getMemberByIdentity(identity) != null);
        }
        if (nickname != null) {
            response.put("nicknameExist", memberService.getMemberByNickname(nickname) != null);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }//바꿈

    @GetMapping("/favorites") // 즐겨찾기 조회 - List<Long> 반환
    public ResponseEntity<?> getFavorites(@RequestParam String identity) {
        MemberDTO memberDTO = memberService.getMemberByIdentity(identity);
        if (memberDTO == null) {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }

        List<Long> favorites = memberService.getFavorites(memberDTO);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    @GetMapping("/finish") // 즐겨찾기 조회 - List<Long> 반환
    public ResponseEntity<?> getFinished(@RequestParam String identity) {
        MemberDTO memberDTO = memberService.getMemberByIdentity(identity);
        if (memberDTO == null) {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }

        List<Long> finished = memberService.getFinished(memberDTO);
        return new ResponseEntity<>(finished, HttpStatus.OK);
    }


//PUT------------------------------------------------------------------------------
    @PutMapping // 멤버 업데이트
    public ResponseEntity<?> updateMember(@RequestBody UpdateBody updateBody) {
        String identity = updateBody.getIdentity();
        String nickname = updateBody.getNickname();

        if (identity != null && nickname != null) {
            return new ResponseEntity<>("아이디와 닉네임 중 하나만 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        if (identity == null && nickname == null) {
            return new ResponseEntity<>("계정을 조회할 요소(아이디/닉네임)가 입력되지 않음", HttpStatus.BAD_REQUEST);
        }

        MemberDTO memberDTO = memberService.getMemberByInfo(identity, nickname);

        if (memberDTO == null) {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }

        if (updateBody.getLevelUp() != 0) {
            memberService.levelUpByMember(memberDTO, updateBody.getLevelUp());
        }

        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    //DELETE------------------------------------------------------------------------------
    @DeleteMapping
    public ResponseEntity<?> deleteMember(@RequestParam(value = "identity", required = false) String identity,
                                          @RequestParam(value = "nickname", required = false) String nickname) {
        if (identity != null && nickname != null) {
            return new ResponseEntity<>("아이디와 닉네임 중 하나만 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        if (identity == null && nickname == null) {
            return new ResponseEntity<>("계정을 조회할 요소(아이디/닉네임)가 입력되지 않음", HttpStatus.BAD_REQUEST);
        }

        MemberDTO memberDTO = memberService.getMemberByInfo(identity, nickname);

        if (memberDTO == null) {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }

        MemberDTO deletedMember = memberService.deleteMemberByMember(memberDTO);

        return new ResponseEntity<>(deletedMember, HttpStatus.OK);
    }

    @DeleteMapping("/favorites") // 즐겨찾기 삭제
    public ResponseEntity<?> deleteFavorites(@RequestParam String identity, @RequestParam List<Long> features) {
        MemberDTO memberDTO = memberService.getMemberByIdentity(identity);
        if (memberDTO == null) {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }

        memberService.deleteFavorites(memberDTO, features);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    @DeleteMapping("/finish") // 공부한 기능 삭제
    public ResponseEntity<?> deleteFinished(@RequestParam String identity, @RequestParam List<Long> features) {
        MemberDTO memberDTO = memberService.getMemberByIdentity(identity);
        if (memberDTO == null) {
            return new ResponseEntity<>("멤버가 발견되지 않음", HttpStatus.NOT_FOUND);
        }

        memberService.deleteFinished(memberDTO, features);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    //------------------------------------------------------------------------------
    @Getter @Setter
    public static class UpdateBody {
        private String identity = null;
        private String nickname = null;
        private int levelUp = 0;
    }
}
