package ai.dataanalytic.apiflow.service;

import ai.dataanalytic.apiflow.model.Request;
import ai.dataanalytic.apiflow.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * ApiService applies business logic to process incoming requests.
 * In this simplified version, it echoes back the request data.
 */
@Slf4j
@Service
public class ApiService {

    /**
     * Applies business logic to the incoming Request and returns a Response.
     *
     * @param request the validated Request object.
     * @return a Mono wrapping the Response.
     */
    public Mono<Response> applyBusinessLogic(Request request) {
        log.info("Processing request with data: {}", request.getRequestData());

        Response response = new Response();
        response.setStatus("Processed");
        response.setResponseMessage("Echo: " + request.getRequestData());
        return Mono.just(response);
    }
}
