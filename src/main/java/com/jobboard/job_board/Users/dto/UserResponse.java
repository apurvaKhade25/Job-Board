package com.jobboard.job_board.Users.dto;

import com.jobboard.job_board.Users.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    @Email
    private String email;

    private Long user_id;

    private String fullName;

    private Role role;
}
