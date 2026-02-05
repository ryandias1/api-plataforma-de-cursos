package br.com.ryan.api_cursos.entity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.ryan.api_cursos.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String email;
    private String password;
    private Role role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role.equals(Role.STUDENT)) return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
        if (role.equals(Role.INSTRUCTOR)) return List.of(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"));
        if (role.equals(Role.ADMIN)) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
