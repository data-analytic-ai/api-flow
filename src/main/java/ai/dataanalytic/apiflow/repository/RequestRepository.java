package ai.dataanalytic.apiflow.repository;

import ai.dataanalytic.apiflow.model.Request;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Reactive repository for managing Request documents.
 * Provides non-blocking CRUD operations for the "requests" collection in MongoDB.
 */
@Repository
public interface RequestRepository extends ReactiveMongoRepository<Request, String> {
    // Additional custom query methods can be defined here if needed.
}
