package rain.property.report;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rain.property.report.dto.HomeResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/", produces = "application/json")
@CrossOrigin(origins = "*")
public final class HomeController {

    @GetMapping("/")
    @ResponseBody
    public Mono<HomeResponse> home() {
        return Mono.just(new HomeResponse("Hello World", "NSW Property"));
    }
}
