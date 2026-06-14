package com.jobboard.job_board.Users;


import com.jobboard.job_board.Users.dto.UserRequest;
import com.jobboard.job_board.Users.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
@Builder
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserResponse CreateUser(UserRequest userrequest) {
        Users users = Users.builder()
                .email(userrequest.getEmail())
                .password(passwordEncoder.encode(userrequest.getPassword()))
                .fullname(userrequest.getFullName())
                .role(userrequest.getRole())
                .build();

        return touserResponse(userRepo.save(users));
    }

    @Transactional
    public UserResponse uploadResume(String email, String resumeUrl) {
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found: " + email));
        System.out.println("Inside uploadResume()");
        System.out.println("Email = " + email);
        System.out.println("URL = " + resumeUrl);
        user.setResumeUrl(resumeUrl);
        System.out.println("After setResumeUrl = " + user.getResumeUrl());
        userRepo.save(user);

        return touserResponse(user);
    }

    // loading all users, no writes
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepo.findAll()
                .stream()
                .map(this::touserResponse)
                .toList();

    }

    // fetching single user, no writes
    @Transactional(readOnly = true)
    public UserResponse getId(Long user_id) {
        Users user = userRepo.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found: " + user_id));
        return touserResponse(user);
    }

    // MAPPER — no DB operation, no transaction needed
    public UserResponse touserResponse(Users u) {
        UserResponse response = new UserResponse();
        response.setUser_id(u.getId());
        response.setEmail(u.getEmail());
        response.setFullName(u.getFullname());
        response.setRole(u.getRole());
        return response;
    }

    public String delete(Long userId, String currentUserEmail) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        if (!user.getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("You can only delete your own profile");
        }

        userRepo.deleteById(userId);
        return "User deleted successfully";
    }

    public UserResponse update(Long userId, @Valid UserRequest userRequest, String currentUserEmail) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        if (!user.getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("You can only update your own profile");
        }

        user.setFullname(userRequest.getFullName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());

        return touserResponse(userRepo.save(user));
    }
}
