package rain.property.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rain.property.report.dto.ErrorResponse;
import rain.property.report.dto.SubscribeRequest;
import rain.property.report.dto.UnsubscribeRequest;
import rain.property.report.exception.EnumNotFoundException;
import rain.property.report.utils.Validator;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RestController
@RequestMapping(path = "/subscriptions", produces = "application/json")
@CrossOrigin(origins = "*")
public final class SubscriptionController {
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionController(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostMapping
    @SuppressWarnings("rawtypes")
    public Mono<ResponseEntity> subscribe(@RequestBody Mono<SubscribeRequest> body) {
        return body.flatMap(request -> {
            if (!Validator.validEmail(request.getEmail())) {
                return Mono.just(new ErrorResponse("email is invalid"))
                        .map(res -> ResponseEntity.badRequest().body(res));
            }

            if (request.getName() == null) {
                return Mono.just(new ErrorResponse("name is missing"))
                        .map(res -> ResponseEntity.badRequest().body(res));
            }

            if (request.getName().isEmpty()) {
                return Mono.just(new ErrorResponse("name is empty"))
                        .map(res -> ResponseEntity.badRequest().body(res));
            }

            return subscriptionRepository.findByEmail(request.getEmail())
                    .switchIfEmpty(Mono.defer(() -> Mono.just(Subscription.newBuilder()
                            .name(request.getName())
                            .type(Subscription.Type.getValue(request.getType().toUpperCase()))
                            .email(request.getEmail())
                            .build()
                    )))
                    .map(subscription -> subscription.builder()
                            .name(request.getName())
                            .type(Subscription.Type.getValue(request.getType().toUpperCase()))
                            .build()
                    )
                    .flatMap(subscriptionRepository::save)
                    .map(ResponseEntity::ok)
                    .map(s -> (ResponseEntity) s)
                    .onErrorResume(e -> {
                        if (e instanceof EnumNotFoundException) {
                            return Mono.just(new ErrorResponse("type must be in " + Arrays.toString(Subscription.Type.values())))
                                    .map(res -> ResponseEntity.badRequest().body(res));
                        } else {
                            return Mono.just(new ErrorResponse(e.getMessage()))
                                    .map(res -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res));
                        }
                    });
        });
    }

    @PostMapping("/unsubscribe")
    @SuppressWarnings("rawtypes")
    public Mono<ResponseEntity> unsubscribe(@RequestBody Mono<UnsubscribeRequest> body) {
        final ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return body.flatMap(r -> subscriptionRepository.findByEmail(r.getEmail()))
                .flatMap(subscriptionRepository::delete)
                .map(v -> response)
                .defaultIfEmpty(response);
    }
}
