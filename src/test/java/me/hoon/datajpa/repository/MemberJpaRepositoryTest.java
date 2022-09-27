package me.hoon.datajpa.repository;

import me.hoon.datajpa.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("hoon");
        Member savedMember = memberJpaRepository.save(member);

        Member foundMember = memberJpaRepository.find(savedMember.getId());

        Assertions.assertThat(foundMember.getUsername()).isEqualTo(member.getUsername());
    }

}