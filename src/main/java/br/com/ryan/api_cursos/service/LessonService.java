package br.com.ryan.api_cursos.service;

import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.ryan.api_cursos.dto.request.ModifyLessonRequest;
import br.com.ryan.api_cursos.dto.request.RegisterLessonRequest;
import br.com.ryan.api_cursos.dto.response.LessonResponse;
import br.com.ryan.api_cursos.entity.Course;
import br.com.ryan.api_cursos.entity.Lesson;
import br.com.ryan.api_cursos.repository.CourseRepository;
import br.com.ryan.api_cursos.repository.LessonRepository;
import br.com.ryan.api_cursos.video.MuxVideoService;
import br.com.ryan.api_cursos.video.VideoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final MuxVideoService muxVideoService;
    private final CourseRepository courseRepository;
    private final AuthService authService;

    public LessonResponse createLesson(RegisterLessonRequest request) {
        Course course = courseRepository.findById(request.course_id()).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
        if (!(authService.getLoggedUser().getId().equals(course.getInstructor().getUser().getId()))) throw new AccessDeniedException("Este curso não é seu");
        Lesson lesson = new Lesson();
        lesson.setNumLesson(request.numLesson());
        lesson.setContent(request.content());
        lesson.setCourse(course);
        course.getLessons().add(lesson);
        lessonRepository.save(lesson);

        return new LessonResponse(lesson.getId(), lesson.getNumLesson(), lesson.getContent(), lesson.getCourse().getId(), lesson.getLink());
    }
    
    public String uploadLesson (UUID id ,MultipartFile file) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("Aula não encontrada"));
        lesson.setAssetId(muxVideoService.uploadVideoParaMux(file));
        lessonRepository.save(lesson);
        return "AssetId definido, video esta subindo!";
    }

    public LessonResponse updateLesson(ModifyLessonRequest request, UUID id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("Aula não encontrada"));
        if (!(lesson.getCourse().getInstructor().getId().equals(authService.getLoggedUser().getId()))) throw new AccessDeniedException("Este curso não é seu");
        if (!(request.content().isBlank())) lesson.setContent(request.content());
        lessonRepository.save(lesson);
        return new LessonResponse(lesson.getId(), lesson.getNumLesson(), lesson.getContent(), lesson.getCourse().getId(), lesson.getLink());
    }

    @Scheduled(fixedRate = 120000)
    private void sincronizarLinks() {
        var lessonSemLink = lessonRepository.findAllByLinkIsNull();
        
        if (lessonSemLink.isEmpty()) return;

        for (Lesson lesson : lessonSemLink) {
            try {
                String pid = muxVideoService.getPlaybackId(lesson.getAssetId());
                if (pid != null) {
                    lesson.setLink("https://player.mux.com/" + pid);
                    lessonRepository.save(lesson);
                    System.out.println("Link atualizado para a aula: " + lesson.getId());
                }
            } catch (Exception e) {
                System.err.println("Vídeo ainda não pronto para Asset: " + lesson.getAssetId());
            }
        }
    }
}
