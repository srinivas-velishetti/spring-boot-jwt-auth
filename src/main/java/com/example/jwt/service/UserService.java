package com.example.jwt.service;
import com.example.jwt.dto.SignupRequest;
import com.example.jwt.entity.UserEntity;
import com.example.jwt.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public void register(SignupRequest req) {
        if (repo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        UserEntity user = UserEntity.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword()))
                .build();
        repo.save(user);
        log.info("User {} registered successfully", user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), List.of());
    }
}