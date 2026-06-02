package com.jobboard.job_board.Config;

import com.jobboard.job_board.Auth.JwtFilter;
import com.jobboard.job_board.Users.UserRepo;
import com.jobboard.job_board.Users.Users;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtFilter jwtFilter;
    private final UserRepo userRepo;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. get authorization header
        String authHeader= request.getHeader("Authorization");

        // 2. check header exists and starts with "Bearer "
        if (authHeader!=null && authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);     // skip if no token
            return;
        }

        // 3. extract token (remove "Bearer " prefix)
        String token=authHeader.substring(7);

        String email=jwtFilter.extractEmail(token);

        // 5. check email exists and user not already authenticated
        if (email!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            // 6. load user from DB
            Users users=userRepo.findByEmail(email).orElse(null);

            // 7. extract role and create authority
            if (users!=null && jwtFilter.validateToken(token,email)){
                String role= jwtFilter.extractRole(token);
                SimpleGrantedAuthority authority=new SimpleGrantedAuthority("ROLE_"+role);

                // 8. create authentication object

                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                        users
                        ,null,
                        List.of(authority));

                // 9. add request details
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 10. set in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
