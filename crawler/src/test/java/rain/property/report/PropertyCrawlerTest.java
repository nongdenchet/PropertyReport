package rain.property.report;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Disabled
public class PropertyCrawlerTest {
    private final PropertyCrawler crawler = new PropertyCrawler();

    @Test
    public void crawlProperties() throws IOException {
        crawler.execute();
    }
}
