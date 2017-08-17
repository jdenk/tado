package com.tado.homework.common.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE;
import static org.apache.commons.lang3.Validate.notEmpty;

@JsonTypeInfo(use = NONE)
public final class GithubIssueDto {

    private final String url;
    private final String title;
    private final String body;

    @JsonCreator
    public GithubIssueDto(@JsonProperty("url") String url,
                          @JsonProperty("title") String title,
                          @JsonProperty("body") String body) {
        notEmpty(url, "url");
        notEmpty(title, "title");

        this.url = url;
        this.title = title;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "GithubIssueDto{" +
            "url='" + url + '\'' +
            ", title='" + title + '\'' +
            ", body='" + body + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubIssueDto that = (GithubIssueDto) o;
        return Objects.equals(url, that.url) &&
            Objects.equals(title, that.title) &&
            Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, body);
    }
}
