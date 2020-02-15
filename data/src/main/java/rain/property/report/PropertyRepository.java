package rain.property.report;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PropertyRepository extends ReactiveCrudRepository<Property, String> {
    // No-op
}
