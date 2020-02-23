package rain.property.report;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PropertyRepository extends ReactiveCrudRepository<Property, String> {
    Flux<Property> findAllByOrderByUpdatedAtDesc();
}
