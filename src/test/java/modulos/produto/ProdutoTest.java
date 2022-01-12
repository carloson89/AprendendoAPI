package modulos.produto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured . RestAssured . *;
import static io.restassured.matcher . RestAssuredMatchers . *;
import static org.hamcrest . Matchers . *;
@DisplayName("Testes de API Rest do modulo de Produto")
public class ProdutoTest {
    private String token;

    @BeforeEach
    public void beforeEach() {
        // Configurando os Dados da API Rest da Lojinha
        baseURI = "http://165.227.93.41";
        // port = 8080;
        basePath = "/lojinha";

        // Obter o token do usuario admin
        this.token = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"usuarioLogin\": \"admin\",\n" +
                        "  \"usuarioSenha\": \"admin\"\n" +
                        "}")
            .when()
                .post("/v2/login")
            .then()
                .extract()
                     .path("data.token");
    }

    @Test
    @DisplayName("Validar os limites proibidos do valor do Produto")
    public void testValidarLimitesProibidosValorProduto() {



        // Tentar inserir um produto com valor 0.00 e validar que a mensagem de erro foi apresentada e o
        // status code retornado foi 422

        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body("{\n" +
                        "  \"produtoNome\": \"Xboxone\",\n" +
                        "  \"produtoValor\": 0.00,\n" +
                        "  \"produtoCores\": [\n" +
                        "    \"preto\"\n" +
                        "  ],\n" +
                        "  \"produtoUrlMock\": \"\",\n" +
                        "  \"componentes\": [\n" +
                        "    {\n" +
                        "      \"componenteNome\": \"controle\",\n" +
                        "      \"componenteQuantidade\": 1\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
        .when()
                .post("/v2/produtos")
        .then()
                .assertThat()
                    .body("error" , equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                    .statusCode(422);
    }
}
