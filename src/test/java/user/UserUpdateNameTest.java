package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;
public class UserUpdateNameTest {
    private UserRequests userRequests;
    @Before
    public void setUp() {
        userRequests = new UserRequests();
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "123456", "Senya");
        userRequests.create(userCreate);
    }
    @Test
    @DisplayName("Update user name")
    @Description("Send post request to /api/auth/user and returns 200 ok")
    public void changeUserName() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");
        UserUpdateName userUpdateName = new UserUpdateName("Esya");
        ValidatableResponse response1 = userRequests.updateName(accessToken, userUpdateName);
        boolean result = response1.extract().path("success");
        assertTrue(result);
        int statusCode = response1.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @After
    public void tearDown() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}
