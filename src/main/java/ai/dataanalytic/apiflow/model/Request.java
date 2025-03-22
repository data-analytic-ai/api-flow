package ai.dataanalytic.apiflow.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents an incoming API request.
 * This document is stored in the "requests" collection in MongoDB.
 */
@Data
@Document(collection = "requests")
public class Request {
    @Id
    private String id;

    @NotBlank(message = "Request data cannot be blank")
    private String requestData;
}
