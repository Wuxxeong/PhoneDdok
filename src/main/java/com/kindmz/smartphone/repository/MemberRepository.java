package com.kindmz.smartphone.repository;

import com.kindmz.smartphone.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByIdentity(String identity);
    Member findByNickname(String nickname);

}
