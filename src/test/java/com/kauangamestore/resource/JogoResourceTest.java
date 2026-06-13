package com.kauangamestore.resource;

import com.kauangamestore.dto.JogoDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class JogoResourceTest {

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testFindAllComHeaders() {
        given()
            .when().get("/jogos?page=0&pageSize=2")
            .then()
            .statusCode(200)
            .header("X-Page", is("0"))
            .header("X-Page-Size", is("2"))
            .header("X-Total-Count", notNullValue());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testCreate() {
        given()
            .contentType(ContentType.JSON)
            .body(new JogoDTO(
                "Jogo Teste Unitario",
                "Descricao Teste",
                150.00,
                10,
                0.0,
                LocalDate.now(),
                1L,
                List.of(1L),
                List.of(1L)
            ))
            .when().post("/jogos")
            .then()
            .statusCode(201)
            .header("Location", notNullValue())
            .body("id", notNullValue())
            .body("titulo", is("Jogo Teste Unitario"))
            .body("estoque", is(10))
            .body("nomeEmpresa", is("Nintendo"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testBuscarPorNome() {
        given()
            .when().get("/jogos/search/nome/zelda?page=0&pageSize=10")
            .then()
            .statusCode(200)
            .header("X-Total-Count", notNullValue())
            .body("[0].titulo", is("The Legend of Zelda"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testUpdate() {
        given()
            .contentType(ContentType.JSON)
            .body(new JogoDTO(
                "Zelda Atualizado",
                "Nova descricao",
                350.00,
                40,
                10.0,
                LocalDate.of(2017, 3, 3),
                1L,
                List.of(4L),
                List.of(1L)
            ))
            .when().put("/jogos/1")
            .then()
            .statusCode(200)
            .body("titulo", is("Zelda Atualizado"))
            .body("categorias[0]", is("Aventura"));
    }
}
