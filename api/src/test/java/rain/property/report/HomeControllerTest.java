package rain.property.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import rain.property.report.dto.HomeResponse;

import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@WebFluxTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getHomeResponse() {
        webTestClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HomeResponse.class)
                .value(HomeResponse::getTitle, equalTo("Hello World"))
                .value(HomeResponse::getContent, equalTo("NSW Property"));
    }
}
