package com.kauangamestore.resource;

import com.kauangamestore.dto.ItemPedidoDTO;
import com.kauangamestore.dto.PedidoDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class PedidoResourceTest {

    @Test
    @TestSecurity(user = "jondoe@gmail.com", roles = {"USER"})
    public void testRealizarPedido() {
        given()
            .contentType(ContentType.JSON)
            .body(new PedidoDTO(
                List.of(new ItemPedidoDTO(3L, 1)),
                2,
                "77000-000",
                "Rua das Flores",
                "123",
                "Palmas"
            ))
            .when().post("/pedidos")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("valorTotal", is(199.9F))
            .body("modoPagamento", is("BOLETO"))
            .body("usuarioEmail", is("jondoe@gmail.com"));
    }

    @Test
    @TestSecurity(user = "jondoe@gmail.com", roles = {"USER"})
    public void testMeusPedidos() {
        given()
            .when().get("/pedidos/meus-pedidos")
            .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testListagemAdministrativa() {
        given()
            .when().get("/pedidos?page=0&pageSize=10")
            .then()
            .statusCode(200)
            .header("X-Total-Count", notNullValue());
    }

    @Test
    public void testSemAuth() {
        given()
            .when().get("/pedidos/meus-pedidos")
            .then()
            .statusCode(401);
    }
}
