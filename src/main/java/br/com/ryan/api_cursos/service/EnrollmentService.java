package br.com.ryan.api_cursos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ryan.api_cursos.dto.response.EnrollmentResponse;
import br.com.ryan.api_cursos.dto.response.UserResponse;
import br.com.ryan.api_cursos.entity.Course;
import br.com.ryan.api_cursos.entity.Enrollment;
import br.com.ryan.api_cursos.entity.Student;
import br.com.ryan.api_cursos.entity.User;
import br.com.ryan.api_cursos.enums.EnrollmentStatus;
import br.com.ryan.api_cursos.repository.CourseRepository;
import br.com.ryan.api_cursos.repository.EnrollmentRepository;
import br.com.ryan.api_cursos.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AuthService authService;
    private final CourseService courseService;

    public EnrollmentResponse enroll (UUID idCurso) {
        Course course = courseRepository.findById(idCurso).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
        User user = authService.getLoggedUser();
        Student student = studentRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalStateException("Usuário já está matriculado neste curso");
        }
        Enrollment enroll = new Enrollment();
        enroll.setCourse(course);
        enroll.setStudent(student);
        enroll.setEnrollmentTime(LocalDateTime.now());
        enroll.setEnrollmentStatus(EnrollmentStatus.ACTIVE);
        Enrollment enrollSaved = enrollmentRepository.save(enroll);
        return toEnrollmentResponse(enrollSaved);
    }

    public List<EnrollmentResponse> getEnrollments (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UUID idStudent = authService.getLoggedUser().getId();
        Student student = studentRepository.findById(idStudent).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
        Page<Enrollment> enrolls = enrollmentRepository.findByStudent(student, pageable);
        return enrolls.stream().map(enroll -> toEnrollmentResponse(enroll)).toList();
    }

    private UserResponse toStudentResponse (Student student) {
        return new UserResponse(student.getUser().getId(), student.getUser().getName(), student.getUser().getEmail());
    }

    private EnrollmentResponse toEnrollmentResponse(Enrollment enrollment) {
        return new EnrollmentResponse(enrollment.getId(), courseService.toResponse(enrollment.getCourse()), toStudentResponse(enrollment.getStudent()), enrollment.getEnrollmentTime(), enrollment.getEnrollmentStatus());
    }
}