package rain.property.report;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public final class Criteria {
    @Id
    private String name;
    private String url;

    public Criteria() {
    }

    public Criteria(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Criteria criteria = (Criteria) o;
        return Objects.equals(name, criteria.name) &&
                Objects.equals(url, criteria.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
