package ai.dataanalytic.apiflow.service;

import ai.dataanalytic.apiflow.model.Request;
import ai.dataanalytic.apiflow.model.Response;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * ApiService applies business logic to process incoming requests.
 * It validates the request, calls an external API with resiliency features,
 * and maps the result to a Response object.
 */
@Slf4j
@Service
public class ApiService {

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    /**
     * Constructor: Initializes the WebClient and injects the pre-configured CircuitBreaker.
     *
     * @param webClientBuilder WebClient builder injected by Spring.
     * @param externalServiceCircuitBreaker CircuitBreaker bean from ResilienceConfig.
     */
    public ApiService(WebClient.Builder webClientBuilder, CircuitBreaker externalServiceCircuitBreaker) {
        this.webClient = webClientBuilder.baseUrl("http://external-api.com").build();
        this.circuitBreaker = externalServiceCircuitBreaker;
    }

    /**
     * Applies business logic to the incoming Request.
     * <p>
     * Steps:
     * 1. Validate the request data.
     * 2. Call an external API with resiliency features:
     *    - Timeout: 5 seconds.
     *    - Retry: 3 attempts with a 1-second delay.
     *    - Circuit Breaker: Prevents further calls if failures exceed threshold.
     * 3. Map the result to a Response object.
     * </p>
     *
     * @param request The incoming Request containing business data.
     * @return A Mono wrapping the processed Response.
     */
    public Mono<Response> applyBusinessLogic(Request request) {
        log.info("Processing request with data: {}", request.getRequestData());

        if (request.getRequestData() == null || request.getRequestData().isEmpty()) {
            log.error("Invalid request: Missing request data");
            return Mono.error(new IllegalArgumentException("Request data cannot be empty"));
        }

        Mono<String> externalCall = webClient.get()
                .uri("/data?query=" + request.getRequestData())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));

        return externalCall.map(data -> {
            Response response = new Response();
            response.setStatus("Processed");
            response.setResponseData("External Data: " + data);
            return response;
        }).doOnError(e -> log.error("Error processing business logic: {}", e.getMessage()));
    }
}
