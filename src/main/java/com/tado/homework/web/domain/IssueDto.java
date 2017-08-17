package com.tado.homework.web.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tado.homework.common.model.GithubIssueDto;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE;

@JsonTypeInfo(use = NONE)
public class IssueDto {

    private final String selfUrl;
    private final String githubUrl;
    private final String title;
    private final String body;


    @JsonCreator
    public IssueDto(@JsonProperty("githubUrl") String githubUrl,
                    @JsonProperty("selfUrl") String selfUrl,
                    @JsonProperty("title") String title,
                    @JsonProperty("body") String body) {
        this.selfUrl = selfUrl;
        this.title = title;
        this.body = body;
        this.githubUrl = githubUrl;
    }

    public String getSelfUrl() {
        return selfUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "IssueDto{" +
            "selfUrl='" + selfUrl + '\'' +
            ", githubUrl='" + githubUrl + '\'' +
            ", title='" + title + '\'' +
            ", body='" + body + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueDto issueDto = (IssueDto) o;
        return Objects.equals(selfUrl, issueDto.selfUrl) &&
            Objects.equals(githubUrl, issueDto.githubUrl) &&
            Objects.equals(title, issueDto.title) &&
            Objects.equals(body, issueDto.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selfUrl, githubUrl, title, body);
    }
 }

