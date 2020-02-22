package rain.property.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SubscribeRequest {
    private String type;
    private String email;
    private String name;

    public SubscribeRequest() {
    }

    public SubscribeRequest(String type, String email, String name) {
        this.type = type;
        this.email = email;
        this.name = name;
    }

    @JsonProperty("type")
    public String getType() {
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
}
