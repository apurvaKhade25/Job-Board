package com.jobboard.job_board.Users;

import com.jobboard.job_board.Users.dto.UserRequest;
import com.jobboard.job_board.Users.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity <UserResponse> CreateUser(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.CreateUser(userRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity <UserResponse> getId(@PathVariable("id") Long user_id){
        return ResponseEntity.ok(userService.getId(user_id));
    }

    @GetMapping("/all")
    public ResponseEntity <List<UserResponse>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

}
