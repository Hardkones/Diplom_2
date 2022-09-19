package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UserUpdateExistingEmailTest {
    private UserRequests userRequests;
    @Before
    public void setUp() {
        userRequests = new UserRequests();
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "123456", "Senya");
        userRequests.create(userCreate);
        UserCreate userCreateSecond = new UserCreate("Eskadna@gmail.com", "123456", "Senya");
        userRequests.create(userCreateSecond);
    }
    @Test
    @DisplayName("Update existing user email")
    @Description("Send post request to /api/auth/user and returns 403 forbidden")
    public void changeExistingUserEmail() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");
        UserUpdateEmail userUpdateEmail = new UserUpdateEmail("Eskadna@gmail.com");
        ValidatableResponse response1 = userRequests.updateEmail(accessToken, userUpdateEmail);
        boolean result = response1.extract().path("success");
        assertFalse(result);
        int statusCode = response1.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @After
    public void tearDown() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
        UserLogin userLoginSecond = new UserLogin("Eskadna@gmail.com", "123456");
        ValidatableResponse response1 = userRequests.login(userLoginSecond);
        String accessToken1 = response1.extract().path("accessToken");
        userRequests.delete(accessToken1);
    }
}
