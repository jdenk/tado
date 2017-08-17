package com.tado.homework.common;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Synchronous rest helper for calling Github Rest API.
 */
@Component
public final class GithubRestTemplate {

    private static final UriTemplate GITHUB_API_TEMPLATE = new UriTemplate("{hostname}/{uri}?access_token={access_token}");


    private final RestTemplate restTemplate;

    private final String hostName;
    private final String accessToken;

    public GithubRestTemplate(RestTemplate restTemplate,
                              @Value("${github.api.hostname}") String hostName,
                              @Value("${github.access.token}") String accessToken) {
        notNull(restTemplate, "restTemplate");
        notEmpty(hostName, "hostName");
        notEmpty(accessToken, "accessToken");

        this.restTemplate = restTemplate;
        this.hostName = hostName;
        this.accessToken = accessToken;
    }

    public <T> ResponseEntity<T> post(final String uri, Object requestEntity, Class<T> responseType) {
        notEmpty(uri, "uri");
        notNull(requestEntity, "requestEntity");
        notNull(responseType, "responseType");

        final String requestUrl = GITHUB_API_TEMPLATE.expand(hostName, uri, accessToken).toASCIIString();
        return restTemplate.postForEntity(requestUrl, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> get(String uri, Class<T> responseType) {
        notEmpty(uri, "uri");

        final String requestUrl = GITHUB_API_TEMPLATE.expand(hostName, uri, accessToken).toASCIIString();
        return restTemplate.getForEntity(requestUrl, responseType);
    }
}

