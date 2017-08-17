package com.tado.homework.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE;
import static org.apache.commons.lang3.Validate.notEmpty;

@JsonTypeInfo(use = NONE)
public final class GithubIssueCreateDto {

    private final String title;

    private final String body;

    @JsonCreator
    public GithubIssueCreateDto(@JsonProperty("title") String title, @JsonProperty("body") String body) {
        notEmpty(title, "title");

        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubIssueCreateDto that = (GithubIssueCreateDto) o;
        return Objects.equals(title, that.title) &&
            Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, body);
    }

    @Override
    public String toString() {
        return "GithubIssueCreateDto{" +
            "title='" + title + '\'' +
            ", body='" + body + '\'' +
            '}';
    }
}
