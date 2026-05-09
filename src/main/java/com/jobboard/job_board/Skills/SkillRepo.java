package com.jobboard.job_board.Skills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepo extends JpaRepository<Skill,Long>{
    Optional<Skill> findByNameIgnoreCase(String name);
}
