package com.fiap.burger.application.bdd;

import com.fiap.burger.api.dto.order.response.ResumedOrderResponseDto;
import com.fiap.burger.application.utils.OrderHelper;
import com.fiap.burger.entity.order.OrderPaymentStatus;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.listener.payment.InMemoryPaymentMessageListener;
import com.fiap.burger.listener.payment.PaymentMessageListenerDto;
import com.google.gson.Gson;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;

public class StepDefinition extends CucumberIntegrationTest {

    @Autowired
    InMemoryPaymentMessageListener paymentMessageListener;

    private Response response;
    private ResumedOrderResponseDto listOrderResponse;

    private String getEndpoint() { return "http://localhost:" + port + "/orders"; }

    @Quando("submeter um novo pedido")
    public ResumedOrderResponseDto submeterUmNovoPedido() {
        var orderRequest = OrderHelper.createOrderRequest();
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequest)
            .when().post(getEndpoint());
        return response.then().extract().as(ResumedOrderResponseDto.class);
    }
    @Entao("o pedido é registrado com sucesso")
    public void pedidoRegistradoComSucesso() {
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(matchesJsonSchemaInClasspath("schemas/ResumedOrderResponseSchema.json"));
    }

    @Dado("que um pedido já foi registrado")
    public void pedidoJaFoiRegistrado() {
        listOrderResponse = submeterUmNovoPedido();
    }
    @Quando("requisitar a busca de um pedido por id")
    public void requisitarBuscaDeUmPedidoPorId() {
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(getEndpoint() + "/{id}", listOrderResponse.id().toString());
    }
    @Entao("pedido é exibido com sucesso detalhando os produtos")
    public void pedidoExibidoComSucessoDetalhandoOsProdutos() {
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(matchesJsonSchemaInClasspath("./schemas/OrderResponseSchema.json"));
    }

    @Quando("realizar o checkout do pedido")
    public void realizarOCheckoutDoPedido() {
        PaymentMessageListenerDto paymentDto = OrderHelper.createPaymentMessageListenerDto(listOrderResponse.id(), OrderPaymentStatus.APROVADO);
        paymentMessageListener.paymentQueueListener(new Gson().toJson(paymentDto));
    }
    @Quando("mudar status para em preparacao")
    public void mudarStatusParaEmPreparacao() {
        var updateRequest = OrderHelper.createUpdateStatusRequest(OrderStatus.EM_PREPARACAO);
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(updateRequest)
            .when().post(getEndpoint() + "/" + listOrderResponse.id() + "/status");
    }

    @Quando("mudar status para pronto")
    public void mudarStatusParaPronto() {
        var updateRequest = OrderHelper.createUpdateStatusRequest(OrderStatus.PRONTO);
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(updateRequest)
            .when().post(getEndpoint() + "/" + listOrderResponse.id() + "/status");
    }
    @Quando("mudar status para finalizado")
    public void mudarStatusParaFinalizado() {
        var updateRequest = OrderHelper.createUpdateStatusRequest(OrderStatus.FINALIZADO);
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(updateRequest)
            .when().post(getEndpoint() + "/" + listOrderResponse.id() + "/status");
    }
    @Entao("pedido deve estar gravado com status finalizado")
    public void pedidoDeveEstarGravadoComStatusFinalizado() {
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(matchesJsonSchemaInClasspath("schemas/ResumedOrderResponseSchema.json"))
            .body("id", equalTo(listOrderResponse.id().intValue()))
            .body("status", equalTo("FINALIZADO"));
    }
    @Quando("requisitar a busca de pedidos em progresso")
    public void requisitarABuscaDePedidosEmProgresso() {
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(getEndpoint() + "/in-progress");
    }
    @Entao("pedido deve estar entre os pedidos retornados")
    public void pedidoDeveEstarEntreOsPedidosRetornados() {
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(matchesJsonSchemaInClasspath("schemas/ListResumedOrderResponseSchema.json"))
            .body("", hasItem(anyOf(hasEntry("id", listOrderResponse.id().intValue()))));
    }

    @Quando("requisitar a busca de pedidos com status recebido")
    public void requisitarABuscaDePedidosComStatusRecebido() {
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(getEndpoint() + "?status=RECEBIDO");
    }
    @Quando("submeter um novo pedido especificando cliente")
    public ResumedOrderResponseDto submeterUmNovoPedidoEspecificandoCliente() {
        var orderRequest = OrderHelper.createOrderWithCustomerRequest();
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequest)
            .when().post(getEndpoint());
        return response.then().extract().as(ResumedOrderResponseDto.class);
    }

    @Entao("o pedido especificando cliente é registrado com sucesso")
    public void pedidoEspecificandoClienteRegistradoComSucesso() {
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(matchesJsonSchemaInClasspath("schemas/ResumedOrderWithClientResponseSchema.json"));
    }

    @Dado("que um pedido com cliente já foi registrado")
    public void queUmPedidoComClienteJáFoiRegistrado() {
        listOrderResponse = submeterUmNovoPedidoEspecificandoCliente();
    }
}
