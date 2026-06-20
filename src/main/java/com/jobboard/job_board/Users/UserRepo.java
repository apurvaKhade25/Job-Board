package com.jobboard.job_board.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Users,Long>{

    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.company WHERE u.email = :email")
    Optional<Users> findByEmailWithCompany(@Param("email") String email);

    List<Users> findByCompanyId(Long id);
}
