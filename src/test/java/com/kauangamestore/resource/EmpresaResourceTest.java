package com.kauangamestore.resource;

import com.kauangamestore.dto.EmpresaDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class EmpresaResourceTest {

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testFindAllComHeaders() {
        given()
            .when().get("/empresas?page=0&pageSize=2")
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
            .body(new EmpresaDTO("Sega", "Japao", "Criadora do Sonic"))
            .when().post("/empresas")
            .then()
            .statusCode(201)
            .header("Location", notNullValue())
            .body("id", notNullValue())
            .body("nome", is("Sega"));
    }
}
