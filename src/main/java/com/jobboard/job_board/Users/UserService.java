package com.jobboard.job_board.Users;


import com.jobboard.job_board.Exception.ResourceNotFoundException;
import com.jobboard.job_board.Users.dto.CompanyUpdateRequest;
import com.jobboard.job_board.Users.dto.UserRequest;
import com.jobboard.job_board.Users.dto.UserResponse;
import com.jobboard.job_board.Users.dto.UserUpdateRequest;
import com.jobboard.job_board.company.Company;
import com.jobboard.job_board.company.CompanyRepo;
import com.jobboard.job_board.company.CompanyService;
import com.jobboard.job_board.company.Dto.CompanyResponse;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
@Builder
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepo companyRepo;
    private final CompanyService companyService;

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
        Users user = userRepo.findByEmailWithCompany(email).orElseThrow(() -> new RuntimeException("User not found: " + email));
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

    // delete user by id, only if the current user is the same as the user being deleted
    public String delete(Long userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

//        if (!user.getEmail().equals(currentUserEmail)) {
//            throw new RuntimeException("You can only delete your own profile");
//        }

        userRepo.deleteById(userId);
        return "User deleted successfully";
    }

    // update user details for both roles
    public UserResponse update(Long userId, @Valid UserUpdateRequest userUpdateRequest) {
        Users users = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));

        if (userUpdateRequest.getFullName() != null) {
            users.setFullname(userUpdateRequest.getFullName());
        }
        if (userUpdateRequest.getEmail() != null) {
            users.setEmail(userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getPassword() != null) {
            users.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }
        return touserResponse(userRepo.save(users));
    }

    // update company details for recruiter
    // returns CompanyResponse not UserResponse
    @Transactional
    public CompanyResponse updateCompany(Long userId, CompanyUpdateRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Company company = user.getCompany();
        if (company == null) {
            throw new ResourceNotFoundException("Recruiter has no company assigned");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            company.setName(request.getName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            company.setEmail(request.getEmail());
        }
        if (request.getWebsite() != null && !request.getWebsite().isBlank()) {
            company.setWebsite(request.getWebsite());
        }
        if (request.getLocation() != null && !request.getLocation().isBlank()) {
            company.setLocation(request.getLocation());
        }

        Company saved = companyRepo.save(company);
        return companyService.toResponse(saved);   // reuse existing mapper
    }

    // MAPPER — no DB operation, no transaction needed
    public UserResponse touserResponse(Users u) {
        UserResponse response = new UserResponse();
        response.setUser_id(u.getId());
        response.setEmail(u.getEmail());
        response.setFullName(u.getFullname());
        response.setRole(u.getRole());
        response.setResumeUrl(u.getResumeUrl());
        response.setCompanyEmail(u.getCompany() != null ? u.getCompany().getEmail() : null);
        response.setCompanyDescription(u.getCompany() != null ? u.getCompany().getDescription() : null);
        response.setCompanyLocation(u.getCompany() != null ? u.getCompany().getLocation() : null);
        response.setCompanyWebsite(u.getCompany() != null ? u.getCompany().getWebsite() : null);
        response.setCompanyName(u.getCompany() != null ? u.getCompany().getName() : null);
        response.setCompanyId(u.getCompany() != null ? u.getCompany().getId() : null);
        return response;
    }
}
