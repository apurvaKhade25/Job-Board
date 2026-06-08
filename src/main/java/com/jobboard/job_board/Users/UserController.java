package com.jobboard.job_board.Users;

import com.jobboard.job_board.Upload.FileUploadService;
import com.jobboard.job_board.Users.dto.UserRequest;
import com.jobboard.job_board.Users.dto.UserResponse;
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
    @PostMapping("/add")
    public ResponseEntity <UserResponse> CreateUser(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.CreateUser(userRequest));
    }

    // get user by id
    // both roles — any logged in user views profile
    @PreAuthorize("hasAnyRole('RECRUITER', 'APPLICANT')")
    @GetMapping("/{id}")
    public ResponseEntity <UserResponse> getId(@PathVariable("id") Long user_id){
        return ResponseEntity.ok(userService.getId(user_id));
    }

    @PreAuthorize("hasRole('APPLICANT')")
    @PostMapping("/uploadResume")
    public ResponseEntity<UserResponse> getResume(@RequestParam MultipartFile file,
                                                  @AuthenticationPrincipal Users current_users){
        String email =current_users.getEmail();

        String resumeUrl= fileUploadService.uploadResume(file);
        return ResponseEntity.ok(userService.uploadResume(email,resumeUrl));
    }

    // history
    // both roles — any logged in user views profile
    @PreAuthorize("hasAnyRole('RECRUITER', 'APPLICANT')")
    @GetMapping("/all")
    public ResponseEntity <List<UserResponse>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

}
