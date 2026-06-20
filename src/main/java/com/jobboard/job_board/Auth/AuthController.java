package com.jobboard.job_board.Auth;

import com.jobboard.job_board.Auth.Dto.AuthRequest;
import com.jobboard.job_board.Auth.Dto.AuthResponse;
import com.jobboard.job_board.Config.JwtAuthFilter;
import com.jobboard.job_board.Users.Role;
import com.jobboard.job_board.Users.UserRepo;
import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.Users.dto.UserRequest;
import com.jobboard.job_board.company.Company;
import com.jobboard.job_board.company.CompanyRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register and login endpoints")
public class AuthController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CompanyRepo companyRepo;


    @Operation(summary = "Register new user",
            description = "Register as RECRUITER or APPLICANT")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest userRequest){

        if (userRepo.findByEmailWithCompany(userRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists "+userRequest.getEmail());
        }

        Users users = Users.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .fullname(userRequest.getFullName())
                .build();
//        userRepo.save(users);

        if (userRequest.getRole()==Role.RECRUITER){

            if (userRequest.getCompanyName()==null){
                throw new RuntimeException("Recruiter must provide company details");
            }

            Company company = Company.builder()
                    .name(userRequest.getCompanyName())
                    .email(userRequest.getCompanyEmail())
                    .website(userRequest.getCompanyWebsite())
                    .location(userRequest.getCompanyLocation())
                    .description(userRequest.getCompanyDescription())
                    .build();
            Company savedCompany=companyRepo.save(company);
            users.setCompany(savedCompany); // ← link recruiter to company

        }
        userRepo.save(users);

        String token = jwtUtil.generateToken(users.getEmail(), userRequest.getRole().name());


        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, users.getEmail(),
                users.getRole().name()));
    }


    @Operation(summary = "Login",
            description = "Returns JWT token on success")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest){

        Users users = userRepo.findByEmailWithCompany(authRequest.getEmail()).orElseThrow(()->new RuntimeException("Invalid " +
                "email or password"));

        if (!passwordEncoder.matches(authRequest.getPassword(), users.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }


        String token = jwtUtil.generateToken(users.getEmail(), users.getRole().name());
        return ResponseEntity.status
                (HttpStatus.OK).body(new AuthResponse(token,users.getEmail(),
                users.getRole().name()));
    }



}
