package com.jobboard.job_board.Users.dto;

import com.jobboard.job_board.Users.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequest {
    @Email
    @Size(max = 100, message="email should be small")
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @NotNull
    private String password;

    @NotNull
    private Role role;

    //optional
//    private MultipartFile resumeUrl;


    // if RECRUITER → provide company details
    // if APPLICANT → leave null
    private Long companyId;
    private String companyName;
    private String companyEmail;
    private String companyWebsite;
    private String companyLocation;
    private String companyDescription;

}
