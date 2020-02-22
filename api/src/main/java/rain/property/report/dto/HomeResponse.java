package rain.property.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class HomeResponse {
    private String title;
    private String content;

    public HomeResponse() {
    }

    public HomeResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }
}
