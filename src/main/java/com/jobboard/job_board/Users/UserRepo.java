package com.jobboard.job_board.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Users,Long>{

    Optional <Users> findByEmail(String email);

}
