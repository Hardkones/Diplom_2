package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

public class UserUpdateUnauthorizedTest {
    private UserRequests userRequests;
    @Before
    public void setUp() {
        userRequests = new UserRequests();
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "123456", "Senya");
        userRequests.create(userCreate);
    }
    @Test
    @DisplayName("Update unauthorized user name")
    @Description("Send post request to /api/auth/user and returns 401 unauthorized")
    public void changeNameForUnauthorizedUser() {
        UserUpdateName userUpdateName = new UserUpdateName("Esya");
        ValidatableResponse response = userRequests.updateNameWithoutToken(userUpdateName);
        boolean result = response.extract().path("success");
        assertFalse(result);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }
    @Test
    @DisplayName("Update unauthorized user email")
    @Description("Send post request to /api/auth/user and returns 401 unauthorized")
    public void changeEmailForUnauthorizedUser() {
        UserUpdateEmail userUpdateEmail = new UserUpdateEmail("Eskadna@gmail.com");
        ValidatableResponse response = userRequests.updateEmailWithoutToken(userUpdateEmail);
        boolean result = response.extract().path("success");
        assertFalse(result);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }
    @After
    public void tearDown() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}
