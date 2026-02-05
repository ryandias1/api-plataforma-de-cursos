package br.com.ryan.api_cursos.video;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MuxVideoService implements VideoService {

    @Value("${mux.token.id}")
    private String tokenId;

    @Value("${mux.token.secret}")
    private String tokenSecret;

    private final WebClient webClient = WebClient.builder().baseUrl("https://api.mux.com").build();

    @Override
    public String uploadVideoParaMux(MultipartFile file) {
        try {
            Map<String, Object> data = this.GetMuxData(webClient);
            String uploadUrl = (String) data.get("url");
            String assetId = (String) data.get("id");
            SendVideoMux(uploadUrl, file);
            return assetId;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao subir vídeo para o Mux", e);
        }
    }

    private Map<String, Object> GetMuxData (WebClient webClient) {
        Map<String, Object> uploadResponse = webClient.post()
                    .uri("/video/v1/uploads")
                    .headers(h -> h.setBasicAuth(tokenId, tokenSecret))
                    .bodyValue(Map.of("new_asset_settings", Map.of("playback_policy", List.of("public"))))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        return (Map<String, Object>) uploadResponse.get("data");
    }

    private void SendVideoMux (String uploadUrl, MultipartFile file) {
        WebClient.create()
                .put()
                .uri(uploadUrl)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.getSize())
                .body(BodyInserters.fromResource(file.getResource()))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
