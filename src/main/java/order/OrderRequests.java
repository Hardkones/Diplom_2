package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.RestClient;

import static io.restassured.RestAssured.given;
public class OrderRequests extends RestClient {
    @Step("Create order with authorized user and with ingredient")
    public ValidatableResponse createOrder(String accessToken, IngredientsJson ingredientsJson) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(ingredientsJson)
                .when()
                .post("/api/orders")
                .then();
    }
    @Step("Create order without ingredient")
    public ValidatableResponse createOrderWithoutIngredient() {
        return given()
                .spec(getBaseSpec())
                .when()
                .post("/api/orders")
                .then();
    }
    @Step("Get list of ingredient for getting hash")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/ingredients")
                .then();
    }
    @Step("Create order without authorization")
    public ValidatableResponse createOrderWithoutLogin(IngredientsJson ingredientsJson) {
        return given()
                .spec(getBaseSpec())
                .body(ingredientsJson)
                .when()
                .post("/api/orders")
                .then();
    }
    @Step("Get order list of authorized user")
    public ValidatableResponse getOrderWithLogin(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders")
                .then();
    }
    @Step("Get order list without user login")
    public ValidatableResponse getOrderWithoutLogin() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/orders")
                .then();
    }
}
