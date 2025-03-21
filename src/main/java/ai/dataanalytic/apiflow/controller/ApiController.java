package ai.dataanalytic.apiflow.controller;

import ai.dataanalytic.apiflow.model.Request;
import ai.dataanalytic.apiflow.model.Response;
import ai.dataanalytic.apiflow.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * API Controller for handling client requests.
 *
 * This controller exposes a reactive endpoint that accepts both JSON and XML data.
 * The incoming Request is processed by the ApiService, and a Response is returned.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    /**
     * Processes an incoming API request.
     *
     * The request is received as a Mono<Request> for non-blocking processing.
     * Business logic is applied via the ApiService, and the resulting Response is returned.
     *
     * @param requestMono A Mono wrapping the Request object.
     * @return A Mono wrapping the Response after business logic is applied.
     */
    @PostMapping(
            value = "/process",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public Mono<Response> processApiRequest(@RequestBody Mono<Request> requestMono) {
        log.info("Received API request");
        return requestMono.flatMap(apiService::applyBusinessLogic);
    }
}
