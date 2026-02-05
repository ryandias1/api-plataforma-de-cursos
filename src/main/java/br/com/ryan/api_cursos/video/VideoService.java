package br.com.ryan.api_cursos.video;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    public String uploadVideoParaMux(MultipartFile file);
}
