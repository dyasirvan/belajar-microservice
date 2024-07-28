package com.example.student.client;

import com.example.student.model.SchoolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SchoolClient {

    @Value("${school.service.url}")
    private String schoolServiceUrl;

    @Value("${school.service.school-path}")
    private String schoolPath;

    private final WebClient webClient;

    public Mono<SchoolResponse> getSchoolById(Long schoolId) {
        return webClient.get()
                .uri(schoolServiceUrl + schoolPath + "/" + schoolId)
                .retrieve()
                .onStatus(httpStatusCode -> !httpStatusCode.is2xxSuccessful(),
                        clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "School not found")))
                .bodyToMono(SchoolResponse.class);
    }

}
