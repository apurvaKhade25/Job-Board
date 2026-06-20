package com.jobboard.job_board.Users;

import com.jobboard.job_board.Upload.FileUploadService;
import com.jobboard.job_board.Users.dto.CompanyUpdateRequest;
import com.jobboard.job_board.Users.dto.UserResponse;
import com.jobboard.job_board.Users.dto.UserUpdateRequest;
import com.jobboard.job_board.company.Dto.CompanyResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FileUploadService fileUploadService;

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
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal Users current_users) {
        // Get the currently authenticated user's email
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(userService.delete(current_users.getId()));
    }

    // see my profile
    // both roles — any logged in user views their own profile
    @Operation(summary = "View my profile (Recruiter & Applicant)",
            description = "RECRUITER & APPLICANT - Views their own profile")
    @PreAuthorize("hasAnyRole('RECRUITER', 'APPLICANT')")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal Users current_users) {
        return ResponseEntity.ok(userService.touserResponse(current_users));
    }

    // update user profile
    // both roles — any logged in user can update their own profile
    @Transactional
    @Operation(summary = "Update user profile (Recruiter & Applicant)",
            description = "RECRUITER & APPLICANT - Updates their own profile")
    @PreAuthorize("hasAnyRole('RECRUITER', 'APPLICANT')")
    @PutMapping("/update")
    public ResponseEntity <UserResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                                   @AuthenticationPrincipal Users current_users) {
        return ResponseEntity.ok(userService.update(current_users.getId(), userUpdateRequest));
    }


    // update company details for recruiter
    @Transactional
    @Operation(summary = "Update company details (Recruiter)",
            description = "RECRUITER - Updates their company details")

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/update/company")
    public ResponseEntity <CompanyResponse> updateCompany(@RequestBody CompanyUpdateRequest companyUpdateRequest,
                                                          @AuthenticationPrincipal Users current_users) {
        return ResponseEntity.ok(userService.updateCompany(current_users.getId(), companyUpdateRequest));
    }
}
