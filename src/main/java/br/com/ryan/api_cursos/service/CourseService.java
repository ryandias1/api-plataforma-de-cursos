package br.com.ryan.api_cursos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.ryan.api_cursos.dto.request.ModifyCourseRequest;
import br.com.ryan.api_cursos.dto.request.RegisterCourseRequest;
import br.com.ryan.api_cursos.dto.response.CourseResponse;
import br.com.ryan.api_cursos.dto.response.LessonResponse;
import br.com.ryan.api_cursos.dto.response.UserResponse;
import br.com.ryan.api_cursos.entity.Course;
import br.com.ryan.api_cursos.entity.Instructor;
import br.com.ryan.api_cursos.entity.User;
import br.com.ryan.api_cursos.enums.Category;
import br.com.ryan.api_cursos.enums.Role;
import br.com.ryan.api_cursos.repository.CourseRepository;
import br.com.ryan.api_cursos.repository.InstructorRepository;
import br.com.ryan.api_cursos.repository.LessonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final AuthService authService;
    private final LessonRepository lessonRepository;

    public CourseResponse registerCourse(RegisterCourseRequest request) {
        Course course = new Course();
        course.setName(request.name());
        course.setDescription(request.description());
        course.setCategory(request.category());
        User user = authService.getLoggedUser();
        if (user.getRole().equals(Role.INSTRUCTOR)) {
            Instructor instructor = instructorRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Instrutor não encontrado"));
            course.setInstructor(instructor);
        } else {
            //Admin vira instrutor para lançar cursos oficiais da plataforma (como usar e outros)
            course.setInstructor(new Instructor(user));
        }
        Course courseSaved = courseRepository.save(course);
        return toResponse(courseSaved);
    }

    public List<CourseResponse> getCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(course -> toResponse(course)).toList();
    }

    public List<CourseResponse> getCoursesByCategory(Category category) {
        List<Course> courses = courseRepository.findByCategory(category);
        return courses.stream().map(course -> toResponse(course)).toList();
    }

    public CourseResponse getCourseById(UUID id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
        return toResponse(course);
    }

    public CourseResponse modifyCourse(ModifyCourseRequest request, UUID id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
        if (!(authService.getLoggedUser().getId().equals(course.getInstructor().getUser().getId()))) throw new AccessDeniedException("Este curso não é seu");
        if (!(request.name().isBlank())) course.setName(request.name());
        if (!(request.description().isBlank())) course.setDescription(request.description());
        if (!(request.category() == null)) course.setCategory(request.category());
        Course courseSaved = courseRepository.save(course);
        return toResponse(courseSaved);
    }

    private CourseResponse toResponse(Course course) {
        List<LessonResponse> lessons = lessonRepository.findByCourse(course).stream().map(lesson -> {
            return new LessonResponse(lesson.getId(), lesson.getNumLesson(), lesson.getContent(), lesson.getCourse().getId(), lesson.getLink());
        }).toList();
        return new CourseResponse(course.getId(), course.getName(), course.getDescription(), course.getCategory(), toUserResponse(course.getInstructor()), lessons);
    }

    private UserResponse toUserResponse (Instructor instructor) {
        return new UserResponse(instructor.getUser().getId(), instructor.getUser().getName(), instructor.getUser().getEmail());
    }
}
