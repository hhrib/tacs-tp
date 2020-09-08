package net.tacs.game;

import java.util.concurrent.Callable;

import org.springframework.web.client.RestTemplate;

public class RestTemplateProvinces implements Callable<String> {

    private RestTemplate restTemplate;

    public RestTemplateProvinces(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String call() throws Exception {

        String url = "some_url";
        String response = restTemplate.getForObject(url, String.class);

        return response;
    }
}