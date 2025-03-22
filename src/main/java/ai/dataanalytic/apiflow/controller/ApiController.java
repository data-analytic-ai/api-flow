package ai.dataanalytic.apiflow.controller;

import ai.dataanalytic.apiflow.model.Request;
import ai.dataanalytic.apiflow.model.Response;
import ai.dataanalytic.apiflow.service.ApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * API Controller for handling client requests.
 * Exposes a reactive endpoint that accepts both JSON and XML data.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    /**
     * Processes an incoming API request.
     * The Request object is validated automatically via @Valid.
     *
     * @param requestMono a Mono wrapping the Request object.
     * @return a Mono wrapping the Response.
     */
    @PostMapping(
            value = "/process",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public Mono<Response> processApiRequest(@RequestBody @Valid Mono<Request> requestMono) {
        log.info("Received API request");
        return requestMono.flatMap(apiService::applyBusinessLogic);
    }
}
