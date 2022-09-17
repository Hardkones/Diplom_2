package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserRequests extends RestClient {
    @Step("Create user")
    public ValidatableResponse create(UserCreate userCreate) {
        return given()
                .spec(getBaseSpec())
                .body(userCreate)
                .when()
                .post("/api/auth/register")
                .then();

    }
    @Step("User login")
    public ValidatableResponse login(UserLogin userLogin) {
        return given()
                .spec(getBaseSpec())
                .body(userLogin)
                .post("/api/auth/login")
                .then();
    }
    @Step("Delete user")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then();
    }
    @Step("Update user email")
    public ValidatableResponse updateEmail(String accessToken, UserUpdateEmail userUpdateEmail) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(userUpdateEmail)
                .when()
                .patch("/api/auth/user")
                .then();
    }
    @Step("Update user name")
    public ValidatableResponse updateName(String accessToken, UserUpdateName userUpdateName) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(userUpdateName)
                .when()
                .patch("/api/auth/user")
                .then();
    }
    @Step("Update unauthorized user name")
    public ValidatableResponse updateNameWithoutToken(UserUpdateName userUpdateName) {
        return given()
                .spec(getBaseSpec())
                .body(userUpdateName)
                .when()
                .patch("/api/auth/user")
                .then();
    }
    @Step("Update unauthorized user email")
    public ValidatableResponse updateEmailWithoutToken(UserUpdateEmail userUpdateEmail) {
        return given()
                .spec(getBaseSpec())
                .body(userUpdateEmail)
                .when()
                .patch("/api/auth/user")
                .then();
    }
}