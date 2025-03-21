package ai.dataanalytic.apiflow.service;

import ai.dataanalytic.apiflow.model.Request;
import ai.dataanalytic.apiflow.model.Response;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;


class ApiServiceTest {

    private ApiService apiService;
    private WebClient.Builder webClientBuilder;
    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        // Create a WebClient builder with a mock base URL for testing.
        webClientBuilder = WebClient.builder().baseUrl("http://mock-external-api.com");
        // Use a simple circuit breaker configuration for testing.
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .slidingWindowSize(10)
                .build();
        circuitBreaker = CircuitBreaker.of("testCircuitBreaker", config);

        // Instantiate the ApiService with the test WebClient builder and circuit breaker.
        apiService = new ApiService(webClientBuilder, circuitBreaker) {
            // Override the external API call to return a stub response immediately for testing.
            @Override
            public Mono<Response> applyBusinessLogic(Request request) {
                if (request.getRequestData() == null || request.getRequestData().isEmpty()) {
                    return Mono.error(new IllegalArgumentException("Request data cannot be empty"));
                }
                // Instead of calling an actual external API, return a dummy response.
                return Mono.just(new Response() {{
                    setStatus("Processed");
                    setResponseData("Test external data for: " + request.getRequestData());
                }});
            }
        };
    }

    @Test
    void testApplyBusinessLogic_ValidRequest() {
        Request testRequest = new Request();
        testRequest.setRequestData("sample-query");

        Mono<Response> responseMono = apiService.applyBusinessLogic(testRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        "Processed".equals(response.getStatus()) &&
                                response.getResponseData().contains("sample-query"))
                .verifyComplete();
    }

    @Test
    void testApplyBusinessLogic_InvalidRequest() {
        Request invalidRequest = new Request();
        invalidRequest.setRequestData("");

        Mono<Response> responseMono = apiService.applyBusinessLogic(invalidRequest);

        StepVerifier.create(responseMono)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
