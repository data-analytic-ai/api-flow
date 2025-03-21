package ai.dataanalytic.apiflow.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a client request.
 * This model is stored in the "requests" collection in MongoDB.
 */
@Data
@Document(collection = "requests")
public class Request {
    @Id
    private String id;
    private String requestData;
}
