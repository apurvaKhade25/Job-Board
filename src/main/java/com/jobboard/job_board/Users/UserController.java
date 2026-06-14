package com.jobboard.job_board.Users;

import com.jobboard.job_board.Upload.FileUploadService;
import com.jobboard.job_board.Users.dto.UserRequest;
import com.jobboard.job_board.Users.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FileUploadService fileUploadService;

    // create user
    @Operation(summary = "Create user profile (Recruiter & Applicant)",
            description = "RECRUITER & APPLICANT - Creates a new user profile")
    @PostMapping("/add")
    public ResponseEntity<UserResponse> CreateUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.CreateUser(userRequest));
    }

    // get user by id
    // both roles — any logged in user views profile
    @Operation(summary = "Get user profile by ID (Recruiter & Applicant)",
            description = "RECRUITER & APPLICANT - Views user profile by ID")
    @PreAuthorize("hasAnyRole('RECRUITER', 'APPLICANT')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getId(@PathVariable("id") Long user_id) {
        return ResponseEntity.ok(userService.getId(user_id));
    }

    @Operation(summary = "Upload resume (Applicant)",
            description = "APPLICANT - Uploads their resume")
    @PreAuthorize("hasRole('APPLICANT')")
    @PostMapping(value = "/uploadResume", consumes = {"multipart/form-data"})
    public ResponseEntity<UserResponse> getResume(@RequestParam MultipartFile file,
                                                  @AuthenticationPrincipal Users current_users) {
        String email = current_users.getEmail();

        String resumeUrl = fileUploadService.uploadResume(file);
        return ResponseEntity.ok(userService.uploadResume(current_users.getEmail(), resumeUrl));
    }

    // history
    // both roles — any logged in user views profile
    @Operation(summary = "Get all user profiles (Recruiter & Applicant)",
            description = "RECRUITER & APPLICANT - Views all user profiles")
    @PreAuthorize("hasAnyRole('RECRUITER', 'APPLICANT')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    // delete user by id
    // both roles — any logged in user can delete their own profile
    @Operation(summary = "Delete user profile (Recruiter & Applicant)",
            description = "RECRUITER & APPLICANT - Deletes their own profile")
    @PreAuthorize("hasAnyRole('RECRUITER', 'APPLICANT')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long user_id) {
        // Get the currently authenticated user's email
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(userService.delete(user_id, currentUserEmail));
    }

    @Operation(summary = "Update user profile (Recruiter & Applicant)",
            description = "RECRUITER & APPLICANT - Updates their own profile")
    @PreAuthorize("hasAnyRole('RECRUITER', 'APPLICANT')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Long user_id,
                                               @Valid @RequestBody UserRequest userRequest) {
        // Get the currently authenticated user's email
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.update(user_id, userRequest, currentUserEmail));
    }
}
