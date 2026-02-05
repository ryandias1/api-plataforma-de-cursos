package br.com.ryan.api_cursos.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.ryan.api_cursos.entity.User;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
            .withIssuer("api-cursos")
            .withSubject(user.getEmail())
            .withExpiresAt(Instant.now().plusSeconds(3600))
            .sign(algorithm);
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("api-cursos")
                .build()
                .verify(token)
                .getSubject();

    }
}
