package com.reactivespring.controller;

import com.reactivespring.controller.FluxAndMonoController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

//@SpringBootTest(classes = FluxAndMonoController.class)
@WebFluxTest(controllers = FluxAndMonoController.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
public class FluxAndMonoControllerTest1 {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testFluxWhenCalledThenReturnFlux() {
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .hasSize(3)
                .contains(1, 2, 3);
    }


    @Test
    public void testHelloMonoWhenCalledThenReturnHello() {
        webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello");
    }

    @Test
    public void testStreamWhenCalledThenReturnStream() {
        webTestClient.get().uri("/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody()
                .take(10)
                .log();
    }
}