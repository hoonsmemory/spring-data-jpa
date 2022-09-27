package me.hoon.datajpa.repository;

import me.hoon.datajpa.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
