package rain.property.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import rain.property.report.exception.EnumNotFoundException;

import java.util.Date;
import java.util.Objects;

@Document(collection = "subscriptions")
public final class Subscription {
    @Id
    private String id;
    @Field(name = "type")
    private final Type type;
    @Field(name = "email")
    private final String email;
    @Field(name = "name")
    private final String name;
    @Field(name = "sentReportAt")
    private final Date sentReportAt;

    public Builder builder() {
        return new Builder().id(id)
                .type(type)
                .email(email)
                .name(name);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public enum Type {
        DAILY, WEEKLY, FORTNIGHTLY;

        public static Type getValue(String value) {
            try {
                return Type.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new EnumNotFoundException();
            }
        }

        public int toDays() {
            switch (this) {
                case DAILY:
                    return 1;
                case WEEKLY:
                    return 7;
                case FORTNIGHTLY:
                    return 14;
            }
            return 1;
        }
    }

    public Subscription(String id, Type type, String email, String name, Date sentReportAt) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.name = name;
        this.sentReportAt = sentReportAt;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonIgnore
    public Date getSentReportAt() {
        return sentReportAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return id.equals(that.id) &&
                type == that.type &&
                email.equals(that.email) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, email, name);
    }

    public static final class Builder {
        private String id;
        private Type type;
        private String email;
        private String name;
        private Date sentReportAt;

        private Builder() {
        }

        public Subscription build() {
            return new Subscription(id, type, email, name, sentReportAt);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder sentReportAt(Date sentReportAt) {
            this.sentReportAt = sentReportAt;
            return this;
        }
    }
}
