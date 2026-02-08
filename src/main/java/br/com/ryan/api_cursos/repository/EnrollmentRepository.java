package br.com.ryan.api_cursos.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ryan.api_cursos.entity.Course;
import br.com.ryan.api_cursos.entity.Enrollment;
import br.com.ryan.api_cursos.entity.Student;



@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    Boolean existsByStudentAndCourse(Student student, Course course);
    Page<Enrollment> findByStudent(Student student, Pageable pageable);
}