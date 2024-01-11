package com.fiap.burger.application.bdd;

import com.fiap.burger.api.dto.order.request.OrderInsertRequestDto;
import com.fiap.burger.api.dto.order.response.ListOrderResponseDto;
import com.fiap.burger.application.utils.OrderHelper;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;

public class StepDefinition {

    private Response response;

    private ListOrderResponseDto listOrderResponse;

    private String ENDPOINT_PRODUCTS = "http://localhost:8080/products";
    private String ENDPOINT_ORDERS = "http://localhost:8081/orders";

    @Dado("que os produtos do pedido já foram registrados")
    public void produtosDoPedidoForamRegistrados() {
        OrderInsertRequestDto request = OrderHelper.createOrderRequest();

        Response r = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(ENDPOINT_PRODUCTS + "?id={ids}", request.toEntity().getProductIds().stream().map(p -> p.toString()).collect(Collectors.joining(",")));

        r.then().body("", hasItem(anyOf(hasEntry("id", 2))));
        r.then().body("", hasItem(anyOf(hasEntry("id", 20))));
    }
    @Quando("submeter um novo pedido")
    public ListOrderResponseDto submeterUmNovoPedido() {
        var orderRequest = OrderHelper.createOrderRequest();
        response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequest)
            .when().post(ENDPOINT_ORDERS);
        return response.then().extract().as(ListOrderResponseDto.class);
    }
    @Entao("o pedido é registrado com sucesso")
    public void pedidoRegistradoComSucesso() {
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(matchesJsonSchemaInClasspath("./schemas/ListOrderResponseSchema.json"));
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
            .get(ENDPOINT_ORDERS + "/{id}", listOrderResponse.id().toString());
    }
    @Entao("pedido é exibido com sucesso detalhando os produtos")
    public void pedidoExibidoComSucessoDetalhandoOsProdutos() {
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(matchesJsonSchemaInClasspath("./schemas/OrderResponseSchema.json"));
    }
}
