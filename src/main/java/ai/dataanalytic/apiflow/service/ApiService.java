package ai.dataanalytic.apiflow.service;

import ai.dataanalytic.apiflow.model.Request;
import ai.dataanalytic.apiflow.model.Response;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * ApiService applies business logic to process incoming requests.
 * It validates the request, performs an external API call with resiliency features,
 * and maps the result to a Response object.
 */
@Slf4j
@Service
public class ApiService {

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    /**
     * Constructor: Initializes WebClient and configures the circuit breaker.
     *
     * @param webClientBuilder WebClient builder injected by Spring.
     */
    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://external-api.com").build();
        CircuitBreakerConfig breakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)                              // Open circuit if 50% of calls fail
                .waitDurationInOpenState(Duration.ofSeconds(10))       // Stay open for 10 seconds
                .slidingWindowSize(10)                                 // Evaluate the last 10 calls
                .build();
        this.circuitBreaker = CircuitBreaker.of("externalService", breakerConfig);
    }

    /**
     * Applies business logic to the incoming Request.
     * <p>
     * Steps:
     * 1. Validate the request data.
     * 2. Call an external API with timeout, retry, and circuit breaker.
     * 3. Map the external API result to a Response object.
     * </p>
     *
     * @param request The incoming Request containing business data.
     * @return A Mono wrapping the processed Response.
     */
    public Mono<Response> applyBusinessLogic(Request request) {
        log.info("Processing request with data: {}", request.getRequestData());

        // Validate the request data.
        if (request.getRequestData() == null || request.getRequestData().isEmpty()) {
            log.error("Invalid request: Missing request data");
            return Mono.error(new IllegalArgumentException("Request data cannot be empty"));
        }

        // Make an external API call with resiliency features.
        Mono<String> externalCall = webClient.get()
                .uri("/data?query=" + request.getRequestData())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))                               // Timeout after 5 seconds.
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))         // Retry 3 times with 1-second delay.
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));  // Apply circuit breaker.

        // Map the external call result to a Response object.
        return externalCall.map(data -> {
            Response response = new Response();
            response.setStatus("Processed");
            response.setResponseData("External Data: " + data);
            return response;
        }).doOnError(e -> log.error("Error during business logic processing: {}", e.getMessage()));
    }

    /**
     * Example of handling a blocking operation by offloading it to a dedicated thread pool.
     *
     * @param request The incoming Request object.
     * @return A Mono wrapping the result of the blocking operation.
     */
    private Mono<String> handleBlockingOperation(Request request) {
        return Mono.fromCallable(() -> {
            log.info("Executing blocking operation for data: {}", request.getRequestData());
            Thread.sleep(3000); // Simulate a blocking delay.
            return "Blocking data for " + request.getRequestData();
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
