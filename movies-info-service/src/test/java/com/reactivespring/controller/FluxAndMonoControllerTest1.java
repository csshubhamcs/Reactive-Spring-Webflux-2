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
import reactor.test.StepVerifier;

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
    public void test_Flux_Approach2(){

        var flux = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(flux)
                .expectNext(1,2,3)
                .verifyComplete();
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
      var stream =  webTestClient.get().uri("/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody()
                .take(10)
                .log();

      StepVerifier.create(stream)
              .expectNext(0L,1L,2L,3L)
              .thenCancel()
              .verify();
    }
}