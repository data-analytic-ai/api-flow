package ai.dataanalytic.apiflow.model;

import lombok.Data;

/**
 * Represents a standard API response.
 * This DTO is used solely for returning data to clients.
 */
@Data
public class Response {
    private String status;
    private String responseMessage;
}
