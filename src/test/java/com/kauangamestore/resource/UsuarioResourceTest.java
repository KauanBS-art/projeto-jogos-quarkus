package com.kauangamestore.resource;

import com.kauangamestore.dto.UsuarioDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class UsuarioResourceTest {

    @Test
    public void testCreate() {
        given()
            .contentType(ContentType.JSON)
            .body(new UsuarioDTO(
                "Novo Usuario Teste",
                "teste@email.com",
                "123456",
                2
            ))
            .when().post("/usuarios")
            .then()
            .statusCode(201)
            .header("Location", notNullValue())
            .body("id", notNullValue())
            .body("email", is("teste@email.com"))
            .body("perfil.label", is("User"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADM"})
    public void testFindAllComHeaders() {
        given()
            .when().get("/usuarios?page=0&pageSize=10")
            .then()
            .statusCode(200)
            .header("X-Page", is("0"))
            .header("X-Page-Size", is("10"))
            .header("X-Total-Count", notNullValue());
    }
}
