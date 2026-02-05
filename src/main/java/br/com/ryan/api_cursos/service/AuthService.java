package br.com.ryan.api_cursos.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ryan.api_cursos.dto.request.SignInRequest;
import br.com.ryan.api_cursos.dto.request.SignUpRequest;
import br.com.ryan.api_cursos.dto.response.SignInResponse;
import br.com.ryan.api_cursos.dto.response.SignUpResponse;
import br.com.ryan.api_cursos.entity.User;
import br.com.ryan.api_cursos.repository.UserRepository;
import br.com.ryan.api_cursos.security.TokenService;

@Service
public class AuthService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public SignInResponse signIn(SignInRequest request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication auth = authenticationManager.authenticate(userAndPass);
        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);
        return new SignInResponse(token);
    }

    public SignUpResponse signUp(SignUpRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        User userSaved = userRepository.save(user);
        return new SignUpResponse(userSaved.getId(), userSaved.getName(), userSaved.getEmail());
    }
}
