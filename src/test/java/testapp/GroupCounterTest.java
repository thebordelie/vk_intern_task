package testapp;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test for group.getCounters method")
public class GroupCounterTest extends BaseTest {

    @Test
    @DisplayName("Checking the response for incorrect input data ")
    public void checkInvalidCounterType() {
        Response response = given()
                .when()
                .get(createURL("invalid_data"));
        assertAll(
                () -> assertEquals("100", response.jsonPath().getString("error_code")),
                () -> assertEquals("PARAM : Invalid parameter counterTypes value  : [invalid_data]", response.jsonPath().getString("error_msg"))
        );
    }

    @ParameterizedTest(name = "type = {0}")
    @CsvFileSource(resources = "/responses.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Checking the response for correct input data")
    public void checkCorrectCounterTypes(String type, String value) {
        Response response = given()
                .when()
                .get(createURL(type));
        assertAll(
                () -> assertEquals(value, response.jsonPath().getString("counters." + type))
        );
    }

    @Test
    @DisplayName("Check status code for correct input data ")
    public void checkStatusCode() {
        Response response = given()
                .when()
                .get(createURL("themes"));
        assertAll(
                () -> assertEquals(200, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("check all counter types")
    public void checkAllCounterTypes() {
        Response response = given()
                .when()
                .get(createURL("themes,photo_albums,members,videos,links,presents,moderators,join_requests,black_list,maybe,photos"));
        assertAll(
                () -> assertEquals(200, response.getStatusCode()),
                () -> assertEquals("75", response.jsonPath().getString("counters.themes")),
                () -> assertEquals("102694", response.jsonPath().getString("counters.members")),
                () -> assertEquals("4", response.jsonPath().getString("counters.videos")),
                () -> assertEquals("1", response.jsonPath().getString("counters.links")),
                () -> assertEquals("-1", response.jsonPath().getString("counters.maybe"))
        );
    }

    @Test
    @DisplayName("check valid and invalid counter types")
    public void checkDifferentInputData() {
        Response response = given()
                .when()
                .get(createURL("themes,invalid_data"));
        assertAll(
                () -> assertEquals("100", response.jsonPath().getString("error_code")),
                () -> assertEquals("PARAM : Invalid parameter counterTypes value  : [themes,invalid_data]", response.jsonPath().getString("error_msg"))
        );
    }

    @Test
    @DisplayName("check equals responses data for different order of input values in requests")
    public void checkEqualsData() {
        Response response1 = given()
                .when()
                .get(createURL("themes,videos"));
        Response response2 = given()
                .when()
                .get(createURL("videos,themes"));
        assertAll(
                () -> assertEquals(response1.jsonPath().getString("counters.themes"), response2.jsonPath().getString("counters.themes")),
                () -> assertEquals(response1.jsonPath().getString("counters.videos"), response2.jsonPath().getString("counters.videos"))
        );
    }

    @Test
    @DisplayName("check empty input data")
    public void checkEmptyInputData() {
        Response response = given()
                .when()
                .get(createURL(""));
        assertAll(
                () -> assertEquals("100", response.jsonPath().getString("error_code")),
                () -> assertEquals("PARAM : Missing required parameter counterTypes", response.jsonPath().getString("error_msg"))
        );
    }

    @Test
    @DisplayName("check incomplete input data")
    public void checkIncompleteInputData() {
        Response response = given()
                .when()
                .get(createURL("themes,"));
        assertAll(
                () -> assertEquals(200, response.getStatusCode()),
                () -> assertEquals("75", response.jsonPath().getString("counters.themes"))
        );
    }

    @Test
    @DisplayName("check duplicate input data")
    public void checkDuplicateInputData() {
        Response response = given()
                .when()
                .get(createURL("themes,themes,themes"));
        assertAll(
                () -> assertEquals(200, response.getStatusCode()),
                () -> assertEquals("75", response.jsonPath().getString("counters.themes"))
        );
    }

    @Test
    @DisplayName("check empty responses for correct input data")
    public void checkEmptyResponses() {
        Response response = given()
                .when()
                .get(createURL("photos"));
        assertAll(
                () -> assertNull(response.jsonPath().getString("counters.photos"))
        );
    }


}
