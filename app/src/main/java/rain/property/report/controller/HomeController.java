package rain.property.report.controller;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping(path = "/", produces = "application/json")
@CrossOrigin(origins = "*")
public final class HomeController {

  public static class Data {
    private final String title;
    private final String content;

    public Data(@NonNull String title, @NonNull String content) {
      Objects.requireNonNull(title);
      Objects.requireNonNull(content);
      this.title = title;
      this.content = content;
    }

    public String getTitle() {
      return title;
    }

    public String getContent() {
      return content;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Data data = (Data) o;
      return title.equals(data.title) &&
          content.equals(data.content);
    }

    @Override
    public int hashCode() {
      return Objects.hash(title, content);
    }
  }

  @GetMapping("/")
  @ResponseBody
  public Mono<Data> home() {
    return Mono.just(new Data("Hello World", "NSW Property"));
  }
}
