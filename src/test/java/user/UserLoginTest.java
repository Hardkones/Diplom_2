package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.*;

import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

public class UserLoginTest {
    private static UserRequests userRequests;

    @BeforeClass
    public static void setUp() {
        userRequests = new UserRequests();
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "123456", "Senya");
        userRequests.create(userCreate);
    }
    @Test
    @DisplayName("Successfully user authorization")
    @Description("Send post request to /api/auth/login and returns 200 ok")
    public void successfullyUserAuthorization() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
        ValidatableResponse response = userRequests.login(userLogin);
        boolean actual = response.extract().path("success");
        assertTrue(actual);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }
    @Test
    @DisplayName("Not enough data for user authorization")
    @Description("Send post request to /api/auth/login and returns 401 unauthorized")
    public void notEnoughDataForUserAuthorization() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "");
        ValidatableResponse response = userRequests.login(userLogin);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
        String message = response.extract().path("message");
        assertEquals("email or password are incorrect", message);
    }
    @Test
    @DisplayName("Wrong data for user authorization")
    @Description("Send post request to /api/auth/login and returns 401 unauthorized")
    public void wrongDataForUserAuthorization() {
        UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123");
        ValidatableResponse response = userRequests.login(userLogin);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
        String message = response.extract().path("message");
        assertEquals("email or password are incorrect", message);
    }
    @AfterClass
    public static void tearDown() {
      UserLogin userLogin = new UserLogin("Hardkones@yandex.ru", "123456");
      ValidatableResponse response = userRequests.login(userLogin);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}
