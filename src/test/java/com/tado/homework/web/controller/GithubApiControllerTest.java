package com.tado.homework.web.controller;

import com.xebialabs.restito.builder.stub.StubHttp;
import com.xebialabs.restito.server.StubServer;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.util.Files;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.resourceContent;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.get;
import static com.xebialabs.restito.semantics.Condition.parameter;
import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.withPostBody;
import static io.restassured.RestAssured.given;
import static io.restassured.module.mockmvc.config.AsyncConfig.withTimeout;
import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonStringEquals;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.glassfish.grizzly.http.util.HttpStatus.CREATED_201;
import static org.glassfish.grizzly.http.util.HttpStatus.NOT_FOUND_404;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.USER_AGENT;


@TestPropertySource(locations = "classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
@ContextConfiguration
public class GithubApiControllerTest {

    private static final StubServer githubMocker;
    private static final int MOCKER_PORT = 33000;

    static {
        githubMocker = new StubServer(MOCKER_PORT);
        githubMocker.start();
        getRuntime().addShutdownHook(new Thread(() -> githubMocker.stop()));
    }

    @Value("${server.port}")
    private int port;

    @Before
    public void setUp() throws Exception {
        githubMocker.clear();

        RestAssuredMockMvc.config = RestAssuredMockMvc.config().asyncConfig(withTimeout(250, TimeUnit.MILLISECONDS));

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = format("http://localhost:%s", port);
        RestAssured.requestSpecification = new RequestSpecBuilder()
            .addHeader(USER_AGENT, "rest-assured-tests")
            .addHeader(ACCEPT, APPLICATION_JSON.toString())
            .addHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
            .build();
    }


    @Test
    public void testGetIssue() {
        whenGithub().match(
            get("/repos/acc/repo/issues/1"),
            parameter("access_token", "test_token")
        ).then(
            ok(),
            resourceContent("json/github/github_issue_response.json")
        );


        given()
            .get("/api/issues/1")
            .then()
            .body(jsonStringEquals(getJsonAsString("json/api/issue_response.json")))
            .statusCode(200);
    }

    @Test
    public void testGetIssueNotFound() {
        whenGithub().match(
            get("/repos/acc/repo/issues/1"),
            parameter("access_token", "test_token")
        ).then(
            status(NOT_FOUND_404)
        );


        given()
            .get("/api/issues/1")
            .then()
            .statusCode(404);
    }

    @Test
    public void testCreateIssue() {
        whenGithub().match(
            post("/repos/acc/repo/issues"),
            withPostBody(),
            parameter("access_token", "test_token")
        ).then(
            status(CREATED_201),
            resourceContent("json/github/github_issue_response.json")
        );


        given()
            .body("{\"title\": \"title\", \"body\": \"body\"}")
            .post("/api/issues")
            .then()
            .statusCode(201);
    }


    private StubHttp whenGithub() {
        return whenHttp(githubMocker);
    }

    private static String getJsonAsString(String fullPathOnClassPath) {
        try {
            return Files.contentOf(ResourceUtils.getFile("classpath:" + fullPathOnClassPath), "utf-8");
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }
}