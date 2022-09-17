package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import user.UserCreate;
import user.UserLogin;
import user.UserRequests;


import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateOrderAuthUserTest {
    private static UserRequests userRequests;

    @BeforeClass
    public static void setUp() {
        userRequests = new UserRequests();
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "123456", "Senya");
        userRequests.create(userCreate);
    }
    @Test
    @DisplayName("Create order with login")
    @Description("Send post request to /api/orders and returns 200 ok")
    public void createOrderWithAuthorizedUser() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");

        OrderRequests orderRequests = new OrderRequests();
        ValidatableResponse response1 = orderRequests.getIngredients();
        String ingredientHash = response1.extract().path("data[0]._id");
        IngredientsJson ingredientsJson = new IngredientsJson(ingredientHash);

        ValidatableResponse response2 = orderRequests.createOrder(accessToken, ingredientsJson);
        boolean result = response2.extract().path("success");
        assertTrue(result);
        int statusCode = response2.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }
    @Test
    @DisplayName("Create order without ingredients")
    @Description("Send post request to /api/orders and returns 400 bad request")
    public void createOrderWithoutIngredients() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");

        OrderRequests orderRequests = new OrderRequests();

        IngredientsJson ingredientsJson = new IngredientsJson();

        ValidatableResponse response2 = orderRequests.createOrder(accessToken, ingredientsJson);
        boolean result = response2.extract().path("success");
        assertFalse(result);
        int statusCode = response2.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);
    }
    @Test
    @DisplayName("Create order with invalid ingredient hash")
    @Description("Send post request to /api/orders and returns 500 internal server error")
    public void createOrderWithInvalidIngredientHash() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");

        OrderRequests orderRequests = new OrderRequests();
        String ingredientHash = "60d3b41abdacab0026";
        IngredientsJson ingredientsJson = new IngredientsJson(ingredientHash);

        ValidatableResponse response2 = orderRequests.createOrder(accessToken, ingredientsJson);
        int statusCode = response2.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }
    @AfterClass
    public static void tearDown() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}
