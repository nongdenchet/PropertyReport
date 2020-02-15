package rain.property.report;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public final class PropertyCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyCrawler.class);
    private static final String URL = "https://www.realestate.com.au/buy/property-house-between-600000-900000-in-nsw/list-1";

    @Scheduled(fixedDelay = 5000)
    public void execute() throws IOException {
        final Document document = Jsoup.connect(URL).get();
        final Elements results = document.getElementsByClass("results-card");
        for (int i = 0; i < results.size(); i++) {
            final Element result = results.get(i);
            final Property property = Property.builder()
                    .address(getAddress(result, i))
                    .imageUrl(getImageUrl(result, i))
                    .url(getUrl(result, i))
                    .price(getPrice(result, i))
                    .type(getType(result, i))
                    .bathrooms(getFeature(result, "general-features__baths"))
                    .carSpaces(getFeature(result, "general-features__cars"))
                    .bedrooms(getFeature(result, "general-features__beds"))
                    .build();
            if (property.isValid()) {
                System.out.println("Valid: " + property);
            } else {
                System.out.println("Invalid: " + property);
            }
        }
    }

    private int getFeature(Element result, String className) {
        final Element type = result.getElementsByClass(className).first();
        if (type != null) {
            try {
                return Integer.parseInt(type.text());
            } catch (NumberFormatException e) {
                LOG.error("Fail to parse: " + type.text(), e);
                return 0;
            }
        } else {
            LOG.warn("feature is missing for: " + className);
            return 0;
        }
    }

    private String getAddress(Element result, int index) {
        final Element address = result.getElementsByClass("residential-card__address-heading").first();
        if (address != null) {
            final Element span = address.getElementsByTag("span").first();
            if (span != null) {
                return span.text();
            } else {
                LOG.warn("address span is missing for: " + result.id() + ", at: " + index);
                return "";
            }
        } else {
            LOG.warn("address is missing for: " + result.id() + ", at: " + index);
            return "";
        }
    }

    private String getType(Element result, int index) {
        final Element type = result.getElementsByClass("residential-card__property-type").first();
        if (type != null) {
            return type.text();
        } else {
            LOG.warn("type is missing for: " + result.id() + ", at: " + index);
            return "";
        }
    }

    private String getPrice(Element result, int index) {
        final Element price = result.getElementsByClass("property-price").first();
        if (price != null) {
            return price.text();
        } else {
            LOG.warn("price is missing for: " + result.id() + ", at: " + index);
            return "";
        }
    }

    private String getUrl(Element result, int index) {
        final Element card = result.getElementsByClass("residential-card__image").first();
        if (card != null) {
            final Element link = card.getElementsByTag("a").first();
            if (link != null) {
                return link.attr("href");
            } else {
                LOG.warn("card image link is missing for: " + result.id() + ", at: " + index);
                return "";
            }
        } else {
            LOG.warn("card image is missing for: " + result.id() + ", at: " + index);
            return "";
        }
    }

    private String getImageUrl(Element result, int index) {
        final Element image = result.getElementsByClass("property-image").first();
        if (image != null) {
            final Element img = image.getElementsByTag("img").first();
            if (img != null) {
                return img.attr("src");
            } else {
                LOG.warn("image link is missing for: " + result.id() + ", at: " + index);
                return "";
            }
        } else {
            LOG.warn("image is missing for: " + result.id() + ", at: " + index);
            return "";
        }
    }
}
