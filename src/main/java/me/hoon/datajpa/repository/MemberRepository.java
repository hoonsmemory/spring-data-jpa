package me.hoon.datajpa.repository;

import me.hoon.datajpa.domain.dto.MemberDto;
import me.hoon.datajpa.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(
            "select m from Member m " +
                    "where m.username = :username " +
                    "and m.age > :age"
    )
    List<Member> findUser(@Param("username") String username,
                          @Param("age") int age);

    @Query(
            "select new me.hoon.datajpa.domain.dto.MemberDto(m.id, m.username, t.name) " +
                    "from Member m " +
                    "join m.team t"
    )
    List<MemberDto> findUsernameDto();

    @Query(
            value = "select m from Member m " +
                    "left join m.team t",
            countQuery = "select count(m) from Member m"
    )
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying
    @Query(
            "update Member m " +
                    "set m.age = m.age + 1 " +
                    "where m.age >= :age "
    )
    int bulkAgePlus(@Param("age") int age);

    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findByUsernameWithLock(String name);
}
