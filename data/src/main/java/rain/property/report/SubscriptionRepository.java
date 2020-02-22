package rain.property.report;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SubscriptionRepository extends ReactiveCrudRepository<Subscription, String> {
    Mono<Subscription> findByEmail(String email);
}
