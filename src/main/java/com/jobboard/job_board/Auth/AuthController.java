package com.jobboard.job_board.Auth;

import com.jobboard.job_board.Auth.Dto.AuthRequest;
import com.jobboard.job_board.Auth.Dto.AuthResponse;
import com.jobboard.job_board.Exception.ResourceNotFoundException;
import com.jobboard.job_board.Users.Role;
import com.jobboard.job_board.Users.UserRepo;
import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.Users.dto.UserRequest;
import com.jobboard.job_board.company.Company;
import com.jobboard.job_board.company.CompanyRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
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
public class AuthController {

    public final UserRepo userRepo;
    public final PasswordEncoder passwordEncoder;
    public final JwtFilter jwtFilter;
    public final CompanyRepo companyRepo;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest userRequest){

        if (userRepo.findByEmail(userRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists"+userRequest.getEmail());
        }

        Users users = Users.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .fullname(userRequest.getFullName())
                .build();
        userRepo.save(users);

        if (userRequest.getRole()== Role.RECRUITER && userRequest.getCompanyId()!=null){
            Company company =
                    companyRepo.findById(userRequest.getCompanyId()).orElseThrow(()-> new ResourceNotFoundException(
                            "Company Not found "+ userRequest.getCompanyId()));

            users.setCompany(company);
        }
        userRepo.save(users);

        String token = jwtFilter.generateToken(users.getEmail(), userRequest.getRole().name());


        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, users.getEmail(),
                users.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest){

        Users users = userRepo.findByEmail(authRequest.getEmail()).orElseThrow(()->new RuntimeException("Invalid " +
                "email or password"));


        String token = jwtFilter.generateToken(users.getEmail(),users.getRole().name());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token,users.getEmail(),
                users.getRole().name()));
    }



}
