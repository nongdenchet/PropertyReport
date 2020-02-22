package rain.property.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ErrorResponse {
    private  String error;

    public ErrorResponse() {
    }

    public ErrorResponse(String error) {
        this.error = error;
    }

    @JsonProperty("error")
    public String getError() {
        return error;
    }
}
