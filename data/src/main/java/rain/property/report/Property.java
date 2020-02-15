package rain.property.report;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "properties")
public final class Property {
    private static final String BASE_URL = "https://www.realestate.com.au";

    @Id
    private String id;
    @Field(name = "address")
    private final String address;
    @Field(name = "url")
    private final String url;
    @Field(name = "imageUrl")
    private final String imageUrl;
    @Field(name = "price")
    private final String price;
    @Field(name = "type")
    private final String type;
    @Field(name = "bedrooms")
    private final int bedrooms;
    @Field(name = "bathrooms")
    private final int bathrooms;
    @Field(name = "carSpaces")
    private final int carSpaces;

    public Property(String id, String address, String url, String imageUrl, String price, String type,
                    int bedrooms, int bathrooms, int carSpaces) {
        this.id = id;
        this.url = url;
        this.address = address;
        this.imageUrl = imageUrl;
        this.price = price;
        this.type = type;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.carSpaces = carSpaces;
    }

    public boolean isValid() {
        return !id.isEmpty();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public String getId() {
        return id;
    }

    public int getCarSpaces() {
        return carSpaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return bedrooms == property.bedrooms &&
                bathrooms == property.bathrooms &&
                carSpaces == property.carSpaces &&
                Objects.equals(id, property.id) &&
                Objects.equals(address, property.address) &&
                Objects.equals(url, property.url) &&
                Objects.equals(imageUrl, property.imageUrl) &&
                Objects.equals(price, property.price) &&
                Objects.equals(type, property.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, url, imageUrl, price, type, bedrooms, bathrooms, carSpaces);
    }

    @Override
    public String toString() {
        return "Property{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                ", bedrooms=" + bedrooms +
                ", bathrooms=" + bathrooms +
                ", carSpaces=" + carSpaces +
                '}';
    }

    public static final class Builder {
        private String address;
        private String url;
        private String imageUrl;
        private String price;
        private String type;
        private int bedrooms;
        private int bathrooms;
        private int carSpaces;

        private Builder() {
        }

        public Property build() {
            return new Property(url, address, BASE_URL + url, imageUrl, price, type, bedrooms, bathrooms, carSpaces);
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder price(String price) {
            this.price = price;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder bedrooms(int bedrooms) {
            this.bedrooms = bedrooms;
            return this;
        }

        public Builder bathrooms(int bathrooms) {
            this.bathrooms = bathrooms;
            return this;
        }

        public Builder carSpaces(int carSpaces) {
            this.carSpaces = carSpaces;
            return this;
        }
    }
}
