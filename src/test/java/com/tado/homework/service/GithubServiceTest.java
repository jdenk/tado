package com.tado.homework.service;

import com.tado.homework.common.model.GithubIssueDto;
import com.tado.homework.web.domain.IssueDto;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class GithubServiceTest {

    @Test
    public void testTranslateIssue() {
        final GithubIssueDto githubIssueDto = new GithubIssueDto("https://api.github.com/repos/acc/repo/issues/3", "title", "body");
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().host("hostname").scheme("http");
        final IssueDto issueCreateDto = GithubService.translateIssue(githubIssueDto, uriComponentsBuilder);

        assertNotNull(issueCreateDto);
        assertThat(issueCreateDto.getTitle(), is("title"));
        assertThat(issueCreateDto.getBody(), is("body"));
        assertThat(issueCreateDto.getGithubUrl(), is("https://api.github.com/repos/acc/repo/issues/3"));
        assertThat(issueCreateDto.getSelfUrl(), is("http://hostname/api/issues/3"));
    }

}