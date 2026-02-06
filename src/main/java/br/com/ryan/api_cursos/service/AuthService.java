package br.com.ryan.api_cursos.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ryan.api_cursos.dto.request.SignInRequest;
import br.com.ryan.api_cursos.dto.request.SignUpRequest;
import br.com.ryan.api_cursos.dto.response.SignInResponse;
import br.com.ryan.api_cursos.dto.response.SignUpResponse;
import br.com.ryan.api_cursos.entity.Instructor;
import br.com.ryan.api_cursos.entity.Student;
import br.com.ryan.api_cursos.entity.User;
import br.com.ryan.api_cursos.enums.Role;
import br.com.ryan.api_cursos.repository.InstructorRepository;
import br.com.ryan.api_cursos.repository.StudentRepository;
import br.com.ryan.api_cursos.repository.UserRepository;
import br.com.ryan.api_cursos.security.TokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

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
        if (userSaved.getRole().equals(Role.STUDENT)) studentRepository.save(new Student(userSaved));
        if (userSaved.getRole().equals(Role.INSTRUCTOR)) instructorRepository.save(new Instructor(userSaved));
        return new SignUpResponse(userSaved.getId(), userSaved.getName(), userSaved.getEmail(), userSaved.getRole());
    }

    public User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    }
}
