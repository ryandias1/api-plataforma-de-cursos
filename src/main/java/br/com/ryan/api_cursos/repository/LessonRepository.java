package br.com.ryan.api_cursos.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ryan.api_cursos.entity.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID>{
}