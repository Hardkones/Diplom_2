package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

public class UserCreateWrongTest {
    private static UserRequests userRequests;

    @BeforeClass
    public static void setUp() {
        userRequests = new UserRequests();
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "123456", "Senya");
        userRequests.create(userCreate);
    }

    @Test
    @DisplayName("Create existing user account")
    @Description("Send post request to /api/auth/register and returns 403 forbidden")
    public void userAccountAlreadyExists() {
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "123456", "Senya");
        ValidatableResponse response = userRequests.create(userCreate);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
        String message = response.extract().path("message");
        assertEquals("User already exists", message);

    }

    @Test
    @DisplayName("Create user account without filling required fields")
    @Description("Send post request to /api/auth/register and returns 403 forbidden")
    public void checkFillingRequiredFieldsForCreateUserAccount() {
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "", "Senya");
        ValidatableResponse response = userRequests.create(userCreate);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
        String message = response.extract().path("message");
        assertEquals("Email, password and name are required fields", message);
    }
    @AfterClass
    public static void tearDown() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}
