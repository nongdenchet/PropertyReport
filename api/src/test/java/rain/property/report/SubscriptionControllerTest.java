package rain.property.report;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import rain.property.report.dto.SubscribeRequest;
import rain.property.report.dto.UnsubscribeRequest;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class SubscriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @AfterEach
    public void tearDown() {
        subscriptionRepository.deleteAll().block();
    }

    @Test
    public void subscribeSuccess() {
        webTestClient.post()
                .uri("/subscriptions")
                .body(Mono.just(new SubscribeRequest("weekly", "quan@gmail.com", "quan")), SubscribeRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.type").isEqualTo("WEEKLY")
                .jsonPath("$.name").isEqualTo("quan")
                .jsonPath("$.email").isEqualTo("quan@gmail.com");
        final Subscription subscription = subscriptionRepository.findByEmail("quan@gmail.com")
                .block();
        assertNotNull(subscription);
        assertNotNull(subscription.getId());
    }

    @Test
    public void subscribeToSameEmail_shouldUpdateRecord() {
        subscriptionRepository.save(Subscription.newBuilder()
                .email("blah@gmail.com")
                .name("quan")
                .type(Subscription.Type.WEEKLY)
                .build())
                .block();
        webTestClient.post()
                .uri("/subscriptions")
                .body(Mono.just(new SubscribeRequest("weekly", "blah@gmail.com", "blah")), SubscribeRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.type").isEqualTo("WEEKLY")
                .jsonPath("$.name").isEqualTo("blah")
                .jsonPath("$.email").isEqualTo("blah@gmail.com");
        final Subscription subscription = subscriptionRepository.findByEmail("blah@gmail.com")
                .block();
        assertNotNull(subscription);
        assertNotNull(subscription.getId());
        assertEquals(1, subscriptionRepository.count().block());
    }

    @Test
    public void unsubscribeToEmail_shouldDeleteFromDatabase() {
        subscriptionRepository.save(Subscription.newBuilder()
                .email("blah@gmail.com")
                .name("quan")
                .type(Subscription.Type.WEEKLY)
                .build())
                .block();
        webTestClient.post()
                .uri("/subscriptions/unsubscribe")
                .body(Mono.just(new UnsubscribeRequest("blah@gmail.com")), UnsubscribeRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
        final Subscription subscription = subscriptionRepository.findByEmail("blah@gmail.com")
                .block();
        assertNull(subscription);
    }

    @Test
    public void unsubscribeWhenEmailNotExist_returnNoContent() {
        webTestClient.post()
                .uri("/subscriptions/unsubscribe")
                .body(Mono.just(new UnsubscribeRequest("hello@gmail.com")), UnsubscribeRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void subscribe_invalidType_returnBadRequest() {
        webTestClient.post()
                .uri("/subscriptions")
                .body(Mono.just(new SubscribeRequest("type", "quan@gmail.com", "quan")), SubscribeRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("type must be in [DAILY, WEEKLY, FORTNIGHTLY]");
        assertNoValue("quan@gmail.com");
    }

    @Test
    public void subscribe_invalidEmail_returnBadRequest() {
        webTestClient.post()
                .uri("/subscriptions")
                .body(Mono.just(new SubscribeRequest("daily", "quan.com", "quan")), SubscribeRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("email is invalid");
        assertNoValue("quan@gmail.com");
    }

    @Test
    public void subscribe_emptyName_returnBadRequest() {
        webTestClient.post()
                .uri("/subscriptions")
                .body(Mono.just(new SubscribeRequest("daily", "quan@gmail.com", "")), SubscribeRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("name is empty");
        assertNoValue("quan@gmail.com");
    }

    @Test
    public void subscribe_missingName_returnBadRequest() {
        webTestClient.post()
                .uri("/subscriptions")
                .body(Mono.just(new SubscribeRequest("daily", "test@gmail.com", null)), SubscribeRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("name is missing");
        assertNoValue("test@gmail.com");
    }

    private void assertNoValue(String email) {
        final Subscription subscription = subscriptionRepository.findByEmail(email)
                .block();
        assertNull(subscription);
    }
}
