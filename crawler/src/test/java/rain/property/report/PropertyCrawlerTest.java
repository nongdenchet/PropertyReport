package rain.property.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

@Disabled
public class PropertyCrawlerTest {
    private PropertyCrawler crawler;

    @Mock
    private PropertyRepository propertyRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        crawler = new PropertyCrawler(propertyRepository);
    }

    @Test
    public void crawlProperties() throws IOException {
        crawler.execute();
    }
}
