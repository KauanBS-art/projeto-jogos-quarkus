package com.kauangamestore.resource;

import com.kauangamestore.dto.AuthDTO;
import com.kauangamestore.dto.UsuarioDTO;
import com.kauangamestore.service.UsuarioService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class AuthResourceTest {

    @Inject
    UsuarioService usuarioService;

    @Test
    public void testLoginSucesso() {
        usuarioService.create(new UsuarioDTO(
            "Usuario Teste Login",
            "teste.login@games.com",
            "123456",
            1
        ));

        given()
            .contentType(ContentType.JSON)
            .body(new AuthDTO("teste.login@games.com", "123456"))
            .when().post("/auth")
            .then()
            .statusCode(200)
            .header("Authorization", notNullValue())
            .body("email", is("teste.login@games.com"))
            .body("perfil.label", is("Adm"));
    }

    @Test
    public void testLoginInvalido() {
        given()
            .contentType(ContentType.JSON)
            .body(new AuthDTO("nao.existe@games.com", "123456"))
            .when().post("/auth")
            .then()
            .statusCode(401)
            .body(is("Email ou senha invalidos"));
    }
}
