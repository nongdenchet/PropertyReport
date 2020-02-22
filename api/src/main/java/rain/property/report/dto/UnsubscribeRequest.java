package rain.property.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class UnsubscribeRequest {
    private String email;

    public UnsubscribeRequest() {
    }

    public UnsubscribeRequest(String email) {
        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
}
