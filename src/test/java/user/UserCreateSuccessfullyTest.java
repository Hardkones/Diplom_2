package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserCreateSuccessfullyTest {
    private UserRequests userRequests;
    @Before
    public void setUp() {
        userRequests = new UserRequests();
    }

    @Test
    @DisplayName("Create user account")
    @Description("Send post request tp /api/auth/register and returns 200 ok")
    public void userAccountCreateSuccessfully() {
        UserCreate userCreate = new UserCreate("Hardkones@yandex.ru", "123456", "Senya");
        ValidatableResponse response = userRequests.create(userCreate);
        String accessToken = response.extract().body().path("accessToken");
        boolean actual = response.extract().body().path("success");
        assertTrue(actual);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        userRequests.delete(accessToken);
    }
}
