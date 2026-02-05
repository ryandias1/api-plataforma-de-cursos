package br.com.ryan.api_cursos.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Instrutores")
public class Instructor {
    @Id
    private UUID id;
    
    @OneToOne
    @MapsId
    private User user;

    @OneToMany(mappedBy = "intructor")
    private List<Course> courses;
}
