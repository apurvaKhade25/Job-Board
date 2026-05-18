package com.jobboard.job_board.Users;


import com.jobboard.job_board.Users.dto.UserRequest;
import com.jobboard.job_board.Users.dto.UserResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class UserService {
    private final UserRepo userRepo;

    public UserResponse CreateUser(UserRequest userrequest){
        Users users=Users.builder()
                .email(userrequest.getEmail())
                .password(userrequest.getPassword())
                .fullname(userrequest.getFullName())
                .role(userrequest.getRole()).
                build();

        return touserResponse(userRepo.save(users));
    }
    public List<UserResponse> getAll(){
        return userRepo.findAll()
                .stream()
                .map(this::touserResponse)
                .toList();

    }

    public UserResponse getId(Long user_id){
        Users user = userRepo.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found: " + user_id));
        return touserResponse(user);
    }

    public UserResponse touserResponse(Users u){
        UserResponse response= new UserResponse();
        response.setUser_id(u.getId());
        response.setEmail(u.getEmail());
        response.setFullName(u.getFullname());
        response.setRole(u.getRole());
        return response;
    }

}
