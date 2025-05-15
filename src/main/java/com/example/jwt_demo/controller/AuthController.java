package com.example.jwt_demo.controller;

import com.example.jwt_demo.auth.JwtTokenProvider;
import com.example.jwt_demo.auth.RefreshTokenStore;
import com.example.jwt_demo.domain.User;
import com.example.jwt_demo.dto.AuthRequest;
import com.example.jwt_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenStore refreshTokenStore;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
        return "회원가입 완료";
    }

    // 로그인 시 Access + Refresh 토큰 발급
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());
        refreshTokenStore.save(userDetails.getUsername(), refreshToken);

        return Map.of(
            "accessToken", accessToken, "refreshToken", refreshToken
        );
    }

    // 토큰 재발금 API
    @PostMapping("/reissue")
    public Map<String, String> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);

        if (!refreshTokenStore.validate(username, refreshToken)) {
            throw new RuntimeException("저장된 Refresh Token과 일치하지 않습니다.");
        }

        String role = "ROLE_USER";
        String newAccessToken = jwtTokenProvider.generateAccessToken(username, role);

        return Map.of("accessToken", newAccessToken);

    }

}
