package rain.property.report;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public final class PropertyCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyCrawler.class);
    private static final String SPEC = "http://selenium-hub:4444/wd/hub";
    private static final String CRAWL_URL = "https://www.realestate.com.au/buy/property-house-between-600000-900000-in-nsw/list-1";

    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyCrawler(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000L)
    public void execute() throws IOException {
        RemoteWebDriver webDriver = null;
        try {
            webDriver = new RemoteWebDriver(new URL(SPEC), DesiredCapabilities.firefox());
            webDriver.get(CRAWL_URL);
            crawlProperties(webDriver);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }

    private void crawlProperties(WebDriver webDriver) {
        final List<Property> properties = new ArrayList<>();
        for (WebElement element : webDriver.findElements(By.className("results-card"))) {
            final Property property = Property.builder()
                    .address(getAddress(element))
                    .imageUrl(getImageUrl(webDriver, element))
                    .url(getUrl(element))
                    .price(getPrice(element))
                    .type(getType(element))
                    .bathrooms(getFeature(element, "general-features__baths"))
                    .carSpaces(getFeature(element, "general-features__cars"))
                    .bedrooms(getFeature(element, "general-features__beds"))
                    .build();
            if (property.isValid()) {
                properties.add(property);
                LOG.info("Valid: " + property);
            } else {
                LOG.warn("Invalid: " + property);
            }
        }
        LOG.info("Saving");
        propertyRepository.saveAll(properties)
                .ignoreElements()
                .block();
    }

    private int getFeature(WebElement element, String className) {
        try {
            final WebElement type = element.findElement(By.className(className));
            try {
                return Integer.parseInt(type.getText());
            } catch (NumberFormatException e) {
                LOG.error("Fail to parse: " + type.getText(), e);
                return 0;
            }
        } catch (NoSuchElementException e) {
            LOG.error("getFeature error", e);
            return 0;
        }
    }

    private String getAddress(WebElement element) {
        try {
            final WebElement address = element.findElement(By.className("residential-card__address-heading"));
            final WebElement span = address.findElement(By.tagName("span"));
            if (span != null) {
                return span.getText();
            } else {
                LOG.warn("address span is missing for: " + element);
                return "";
            }
        } catch (NoSuchElementException e) {
            LOG.error("getAddress error", e);
            return "";
        }
    }

    private String getType(WebElement element) {
        try {
            final WebElement type = element.findElement(By.className("residential-card__property-type"));
            return type.getText();
        } catch (NoSuchElementException e) {
            LOG.error("getType error", e);
            return "";
        }
    }

    private String getPrice(WebElement element) {
        try {
            final WebElement price = element.findElement(By.className("property-price"));
            return price.getText();
        } catch (NoSuchElementException e) {
            LOG.error("getPrice error", e);
            return "";
        }
    }

    private String getUrl(WebElement element) {
        try {
            final WebElement card = element.findElement(By.className("residential-card__image"));
            final WebElement link = card.findElement(By.tagName("a"));
            if (link != null) {
                return link.getAttribute("href");
            } else {
                LOG.warn("card image link is missing for: " + element);
                return "";
            }
        } catch (NoSuchElementException e) {
            LOG.error("getUrl error", e);
            return "";
        }
    }

    private String getImageUrl(WebDriver webDriver, WebElement element) {
        try {
            final WebElement image = element.findElement(By.className("property-image"));
            final WebElement img = image.findElement(By.tagName("img"));
            if (img != null) {
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", img);
                return img.getAttribute("src");
            } else {
                LOG.warn("img link is missing for: " + element);
                return "";
            }
        } catch (NoSuchElementException e) {
            LOG.error("address error", e);
            return "";
        }
    }
}
