package com.kauangamestore.resource;

import com.kauangamestore.dto.CategoriaDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class CategoriaResourceTest {

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testFindAllComHeaders() {
        given()
            .when().get("/categorias?page=0&pageSize=2")
            .then()
            .statusCode(200)
            .header("X-Page", is("0"))
            .header("X-Page-Size", is("2"))
            .header("X-Total-Count", notNullValue());
    }

    @Test
    public void testFindAllSemAuth() {
        given()
            .when().get("/categorias")
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testCreate() {
        given()
            .contentType(ContentType.JSON)
            .body(new CategoriaDTO("Roguelike Teste"))
            .when().post("/categorias")
            .then()
            .statusCode(201)
            .header("Location", notNullValue())
            .body("id", notNullValue())
            .body("nome", is("Roguelike Teste"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testBuscarPorNome() {
        given()
            .when().get("/categorias/search/nome/avent?page=0&pageSize=10")
            .then()
            .statusCode(200)
            .header("X-Total-Count", notNullValue())
            .body("[0].nome", is("Aventura"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testDelete() {
        Integer id = given()
            .contentType(ContentType.JSON)
            .body(new CategoriaDTO("Delete Categoria Teste"))
            .when().post("/categorias")
            .then().statusCode(201).extract().path("id");

        given()
            .when().delete("/categorias/" + id)
            .then()
            .statusCode(204);
    }
}
