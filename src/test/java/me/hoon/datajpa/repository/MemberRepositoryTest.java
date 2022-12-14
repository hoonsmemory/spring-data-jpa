package me.hoon.datajpa.repository;

import me.hoon.datajpa.domain.dto.MemberDto;
import me.hoon.datajpa.domain.entity.Member;
import me.hoon.datajpa.domain.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Rollback(false)
@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("hoon");
        Member savedMember = memberRepository.save(member);

        Member foundMember = memberRepository.findById(member.getId()).get();

        assertThat(foundMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void findUser() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void findUsernameDto() {
        Team team = new Team("TeamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        m1.setTeam(team);
        m2.setTeam(team);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<MemberDto> usernameDto = memberRepository.findUsernameDto();

        for (MemberDto memberDto : usernameDto) {
            System.out.println("dto = " + memberDto);
        }
    }

    @Test
    public void pagingTest() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));
        memberRepository.save(new Member("member10", 10));
        memberRepository.save(new Member("member11", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "username"));


        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent(); //????????? ?????????
        assertThat(content.size()).isEqualTo(3); //????????? ????????? ???
        assertThat(page.getTotalElements()).isEqualTo(11); //?????? ????????? ???
        assertThat(page.getNumber()).isEqualTo(0); //????????? ??????
        assertThat(page.getTotalPages()).isEqualTo(4); //?????? ????????? ??????
        assertThat(page.isFirst()).isTrue(); //????????? ?????????????
        assertThat(page.hasNext()).isTrue(); //?????? ???????????? ??????????
    }

    @Test
    public void bulkAgePlus() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 50));

        int count = memberRepository.bulkAgePlus(20);

        Assertions.assertThat(count).isEqualTo(4);
    }

}