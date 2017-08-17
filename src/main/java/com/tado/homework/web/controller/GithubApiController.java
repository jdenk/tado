package com.tado.homework.web.controller;

import com.tado.homework.service.GithubService;
import com.tado.homework.web.domain.IssueCreateDto;
import com.tado.homework.web.domain.IssueDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class GithubApiController {

    public static final String ISSUES_URI = "/api/issues";
    public static final String ISSUE_URI =  ISSUES_URI +"/{issueId}";

    private final GithubService githubService;

    public GithubApiController(GithubService githubService) {
        this.githubService = githubService;
    }

    @PostMapping(ISSUES_URI)
    public ResponseEntity<IssueDto> createIssue(@Valid @RequestBody IssueCreateDto issueCreateDto, UriComponentsBuilder uriComponentsBuilder) {
        final IssueDto createdIssue = this.githubService.createIssue(issueCreateDto, uriComponentsBuilder);
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(URI.create(createdIssue.getSelfUrl()));
        return new ResponseEntity<>(createdIssue, responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping(ISSUE_URI)
    public ResponseEntity<IssueDto> getIssue(@PathVariable String issueId, UriComponentsBuilder uriComponentsBuilder) {
        final IssueDto githubIssue = this.githubService.getIssue(issueId, uriComponentsBuilder);
        return new ResponseEntity<>(githubIssue, HttpStatus.OK);
    }

}
