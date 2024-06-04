package com.asteria.productcartservice;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    private static final Gson gson = new Gson();
    private static final Header CONTENT_TYPE = new Header("Content-Type", "application/json");

    private static final String PRODUCT_ONE_NAME = "Product One";
    private static final String PRODUCT_ONE_DESCRIPTION = "Product One Description";
    private static final double PRODUCT_PRICE_ONE = 10.0;
    private static final Product PRODUCT_ONE = new Product(PRODUCT_ONE_NAME, PRODUCT_ONE_DESCRIPTION, PRODUCT_PRICE_ONE);
    private static final String PRODUCT_TWO_NAME = "Product Two";
    private static final String PRODUCT_TWO_DESCRIPTION = "Product Two Description";
    private static final double PRODUCT_PRICE_TWO = 20.0;
    private static final Product PRODUCT_TWO = new Product(PRODUCT_TWO_NAME, PRODUCT_TWO_DESCRIPTION, PRODUCT_PRICE_TWO);

    private Long productIdOne;
    private Long productIdTwo;
    private Long cartId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @Order(1)
    void receive404WhenCartDoesNotExist() {
        given().
                when().
                get("/cart/-1").
                then().
                statusCode(404);
    }

    @Test
    @Order(2)
    void receive404WhenProductDoesNotExist() {
        given().
                when().
                get("/product/-1").
                then().
                statusCode(404);
    }

    @Test
    @Order(3)
    void productCreation() {
        Response productOneId = given().body(gson.toJson(PRODUCT_ONE))
                .header(CONTENT_TYPE)
                .post("/product")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Response productTwoId = given().body(gson.toJson(PRODUCT_TWO))
                .header(CONTENT_TYPE)
                .post("/product")
                .then()
                .statusCode(200)
                .extract()
                .response();

        productIdOne = productOneId.jsonPath().getLong("id");
        productIdTwo = productTwoId.jsonPath().getLong("id");

        assertThat(productIdOne).isNotNull();
        assertThat(productIdTwo).isNotNull();
    }

    @Test
    @Order(4)
    void testCartCreation() {
        Response cartIdResponse = given()
                .header(CONTENT_TYPE)
                .post("/cart")
                .then()
                .statusCode(200)
                .extract()
                .response();

        cartId = cartIdResponse.jsonPath().getLong("id");
        assertThat(cartId).isNotNull();
    }

    @Test
    @Order(5)
    void retreiveEmptyCart() {
        Response getCartResponse = given()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Object> lineItems = getCartResponse.jsonPath().getList("lineItems");
        assertThat(lineItems).isNotNull();
        assertThat(lineItems).isEmpty();
    }

    @Test
    @Order(6)
    void addProductOneToCart() {
        given()
                .put("/cart/" + cartId + "/add-product/" + productIdOne + "?quantity=2")
                .then()
                .statusCode(200);

        Response getCartResponse = given()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Object> lineItems = getCartResponse.jsonPath().getList("lineItems");
        assertThat(lineItems).isNotNull();
        assertThat(lineItems).hasSize(1);
        String total = getCartResponse.jsonPath().getString("total");
        assertThat(Double.valueOf(total)).isEqualTo(20.0);
    }

    @Test
    @Order(7)
    void addProductTwoToCart() {
        given()
                .put("/cart/" + cartId + "/add-product/" + productIdTwo + "?quantity=4")
                .then()
                .statusCode(200);

        Response getCartResponse = given()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Object> lineItems = getCartResponse.jsonPath().getList("lineItems");
        assertThat(lineItems).isNotNull();
        assertThat(lineItems).hasSize(2);
        String total = getCartResponse.jsonPath().getString("total");
        assertThat(Double.valueOf(total)).isEqualTo(100.0);
    }

    @Test
    @Order(8)
    void removeProductTwoFromCart() {
        given()
                .put("/cart/" + cartId + "/remove-product/" + productIdTwo + "?quantity=2")
                .then()
                .statusCode(200);

        Response getCartResponse = given()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Object> lineItems = getCartResponse.jsonPath().getList("lineItems");
        assertThat(lineItems).isNotNull();
        assertThat(lineItems).hasSize(2);
        String total = getCartResponse.jsonPath().getString("total");
        assertThat(Double.valueOf(total)).isEqualTo(60.0);
    }

    @Test
    @Order(9)
    void clearCart() {
        given()
                .put("/cart/" + cartId + "/clear")
                .then()
                .statusCode(200);

        Response getCartResponse = given()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Object> lineItems = getCartResponse.jsonPath().getList("lineItems");
        assertThat(lineItems).isNotNull();
        assertThat(lineItems).hasSize(0);
    }

    @Test
    @Order(10)
    void removeProducts() {
        given()
                .delete("/product/" + productIdOne)
                .then()
                .statusCode(200);

        given()
                .delete("/product/" + productIdTwo)
                .then()
                .statusCode(200);
    }

    @Test
    @Order(11)
    void receive404WhenAccessingDeletedProducts() {
        given().
                when().
                get("/product/" + productIdOne).
                then().
                statusCode(404);
        given().
                when().
                get("/product/" + productIdTwo).
                then().
                statusCode(404);
    }


    private static class Product {
        private String name;
        private String description;
        private double price;

        public Product(String name, String description, double price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public double getPrice() {
            return price;
        }
    }
}
