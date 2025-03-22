//package ai.dataanalytic.apiflow.config;
//
//import io.github.resilience4j.circuitbreaker.CircuitBreaker;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.Duration;
//
///**
// * Configures resiliency features for APIFlow using Resilience4j.
// * This configuration creates a Circuit Breaker bean for external service calls.
// */
//@Configuration
//public class ResilienceConfig {
//
//    /**
//     * Creates a CircuitBreaker bean configured to:
//     * - Open the circuit if 50% of calls fail within a sliding window of 10 calls.
//     * - Remain open for 10 seconds before trying to close the circuit.
//     *
//     * @return a configured CircuitBreaker instance.
//     */
//    @Bean
//    public CircuitBreaker externalServiceCircuitBreaker() {
//        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
//                .failureRateThreshold(50)
//                .waitDurationInOpenState(Duration.ofSeconds(10))
//                .slidingWindowSize(10)
//                .build();
//        return CircuitBreaker.of("externalService", config);
//    }
//}
