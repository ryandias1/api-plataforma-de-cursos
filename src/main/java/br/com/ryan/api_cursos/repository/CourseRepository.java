package br.com.ryan.api_cursos.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ryan.api_cursos.entity.Course;
import java.util.List;
import br.com.ryan.api_cursos.enums.Category;


@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findByCategory(Category category);
}