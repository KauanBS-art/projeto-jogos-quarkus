package com.kauangamestore.resource;

import com.kauangamestore.dto.PlataformaDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class PlataformaResourceTest {

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testFindAllComHeaders() {
        given()
            .when().get("/plataformas?page=0&pageSize=2")
            .then()
            .statusCode(200)
            .header("X-Page", is("0"))
            .header("X-Page-Size", is("2"))
            .header("X-Total-Count", notNullValue());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testFindById() {
        given()
            .when().get("/plataformas/1")
            .then()
            .statusCode(200)
            .body("nome", is("PC"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testCreate() {
        given()
            .contentType(ContentType.JSON)
            .body(new PlataformaDTO("Mega Drive"))
            .when().post("/plataformas")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("nome", is("Mega Drive"));
    }
}
