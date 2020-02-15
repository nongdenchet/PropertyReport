package rain.property.report;

import java.util.Objects;

public final class Property {
    private final String id;
    private final String address;
    private final String url;
    private final String imageUrl;
    private final String price;
    private final String type;
    private final int bedrooms;
    private final int bathrooms;
    private final int carSpaces;

    private Property(Builder builder) {
        this.id = builder.url;
        this.address = builder.address;
        this.url = builder.url;
        this.imageUrl = builder.imageUrl;
        this.price = builder.price;
        this.type = builder.type;
        this.bedrooms = builder.bedrooms;
        this.bathrooms = builder.bathrooms;
        this.carSpaces = builder.carSpaces;
    }

    public static Builder newProperty() {
        return new Builder();
    }

    public boolean isValid() {
        return !id.isEmpty();
    }

    public static Builder builder() {
        return new Builder();
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
            return new Property(this);
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
