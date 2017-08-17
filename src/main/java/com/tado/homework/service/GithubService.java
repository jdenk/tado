package com.tado.homework.service;

import com.tado.homework.common.GithubRestTemplate;
import com.tado.homework.common.model.GithubIssueCreateDto;
import com.tado.homework.common.model.GithubIssueDto;
import com.tado.homework.web.domain.IssueCreateDto;
import com.tado.homework.web.domain.IssueDto;
import com.tado.homework.web.exception.IssueNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

import java.util.Map;

import static com.tado.homework.web.controller.GithubApiController.ISSUE_URI;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

@Service
public final class GithubService {
    private static final String GITHUB_ISSUES_URI = "repos/{account}/{repository}/issues";
    private static final String ISSUE_ID_VAR = "issueId";

    private static final UriTemplate GITHUB_ISSUES_URI_TEMPLATE = new UriTemplate(GITHUB_ISSUES_URI);
    private static final UriTemplate GITHUB_ISSUE_URI_TEMPLATE = new UriTemplate(GITHUB_ISSUES_URI + "/{" + ISSUE_ID_VAR + "}");

    private final GithubRestTemplate githubRestTemplate;

    private final String account;
    private final String repository;

    public GithubService(GithubRestTemplate githubRestTemplate,
                         @Value("${github.api.account}") String account,
                         @Value("${github.api.repository}") String repository
    ) {
        notNull(githubRestTemplate, "githubRestTemplate");
        notEmpty(account, "account");
        notEmpty(repository, "repository");

        this.githubRestTemplate = githubRestTemplate;
        this.account = account;
        this.repository = repository;
    }

    public IssueDto createIssue(IssueCreateDto issueCreateDto, UriComponentsBuilder uriComponentsBuilder) {
        final String issuesUri = GITHUB_ISSUES_URI_TEMPLATE.expand(account, repository).toASCIIString();
        final GithubIssueDto createdIssue = githubRestTemplate.post(issuesUri, translateIssueCreate(issueCreateDto), GithubIssueDto.class).getBody();

        return translateIssue(createdIssue, uriComponentsBuilder);
    }

    public IssueDto getIssue(String issueId, UriComponentsBuilder uriComponentsBuilder) {
        final String issueUri = GITHUB_ISSUE_URI_TEMPLATE.expand(account, repository, issueId).toASCIIString();

        try {
            return translateIssue(githubRestTemplate.get(issueUri, GithubIssueDto.class).getBody(), uriComponentsBuilder);
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new IssueNotFoundException();
            }
            throw e;
        }
    }

    static GithubIssueCreateDto translateIssueCreate(IssueCreateDto issueCreateDto) {
        return new GithubIssueCreateDto(issueCreateDto.getTitle(), issueCreateDto.getBody());
    }

    static IssueDto translateIssue(GithubIssueDto githubIssueDto, UriComponentsBuilder uriComponentsBuilder) {
        final UriComponentsBuilder githubUriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(githubIssueDto.getUrl());
        final String path = githubUriComponentsBuilder.build().getPath();
        final Map<String, String> variables = GITHUB_ISSUE_URI_TEMPLATE.match(path);

        final UriComponents uriComponents = uriComponentsBuilder.path(ISSUE_URI).buildAndExpand(variables.get(ISSUE_ID_VAR));
        return new IssueDto(githubIssueDto.getUrl(), uriComponents.toUriString(), githubIssueDto.getTitle(), githubIssueDto.getBody());
    }
}
