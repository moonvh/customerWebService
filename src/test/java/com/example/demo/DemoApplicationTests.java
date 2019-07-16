package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {
    @Autowired
    private WebTestClient testClient;

    @Test
    public void topCustomersPerYear_test() throws Exception {

        testClient.get().uri("/topCustomersPerYear")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);

    }

    @Test
    public void customersWithNoDeal_test() throws Exception {

        testClient.get().uri("/customersWithNoDeal")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);

    }
    @Test
    public void bigstSumAmtPerManagementInfo_test() throws Exception {

        testClient.get().uri("/bigstSumAmtPerManagementInfo")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);

    }
    @Test
    public void managementInfo_test_ok() throws Exception {

        testClient.get().uri("/managementInfo/B")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);

    }
    @Test
    public void managementInfo_test_false() throws Exception {

        testClient.get().uri("/managementInfo/A")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);

    }



}
