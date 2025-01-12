package restfulbooker;

import java.io.InputStream;

import org.testng.annotations.Test;

import Data.restfulbookerdata.BookingData;
import static Data.restfulbookerdata.BookingDataBuilder.getBookingData;
import static Data.restfulbookerdata.BookingDataBuilder.getPartialBookingData;
import Data.restfulbookerdata.PartialBookingData;
import static Data.restfulbookerdata.TokenBuilder.getToken;
import Data.restfulbookerdata.Tokencreds;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import static io.restassured.RestAssured.given;
import io.restassured.module.jsv.JsonSchemaValidator;

@Epic("Rest Assured POC - Example Tests")
@Feature("JSON Schema Validation using rest-assured")
public class JsonSchemaValidationTest extends BaseSetup {

    private int bookingId;

    @Test
    @Description("Example test for checking json schema for new booking - Post request")
    @Severity(SeverityLevel.CRITICAL)
    @Story("JSON Schema Validation using rest-assured")
    public void testCreateBookingJsonSchema() {

        InputStream createBookingJsonSchema = getClass().getClassLoader()
                .getResourceAsStream("createbookingjsonschema.json");
        BookingData newBooking = getBookingData();
        bookingId = given().body(newBooking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(createBookingJsonSchema))
                .and()
                .extract()
                .path("bookingid");
    }

    @Test
    @Description("Example test for checking json schema after getting a booking - get request")
    @Severity(SeverityLevel.CRITICAL)
    @Story("JSON Schema Validation using rest-assured")
    public void testGetBookingJsonSchema() {

        InputStream getBookingJsonSchema = getClass().getClassLoader()
                .getResourceAsStream("getbookingjsonschema.json");

        given().when()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(getBookingJsonSchema));
    }

    @Test
    @Description("Example test for checking json schema after updating a booking - Put request")
    @Severity(SeverityLevel.NORMAL)
    @Story("JSON Schema Validation using rest-assured")
    public void testUpdateBookingJsonSchema() {
        InputStream updateBookingJsonSchema = getClass().getClassLoader()
                .getResourceAsStream("updatebookingjsonschema.json");

        BookingData updatedBooking = getBookingData();
        given().when()
                .body(updatedBooking)
                .get("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(updateBookingJsonSchema));
    }

    @Test
    @Description("Example test for checking json schema after updating a booking partially - Patch request")
    @Severity(SeverityLevel.NORMAL)
    @Story("JSON Schema Validation using rest-assured")
    public void testUpdatePartialBookingJsonSchema() {
        InputStream updatePartialBookingJsonSchema = getClass().getClassLoader()
                .getResourceAsStream("updatepartialbookingjsonschema.json");

        PartialBookingData partialUpdateBooking = getPartialBookingData();
        given().when()
                .body(partialUpdateBooking)
                .get("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(updatePartialBookingJsonSchema));
    }

    @Test
    @Description("Example test for checking json schema for token authentication - Post request")
    @Severity(SeverityLevel.BLOCKER)
    @Story("JSON Schema Validation using rest-assured")
    public void testCreateJsonSchema() {
        InputStream createTokenJsonSchema = getClass().getClassLoader()
                .getResourceAsStream("createtokenjsonschema.json");

        Tokencreds tokenCreds = getToken();
        given().body(tokenCreds)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(createTokenJsonSchema));
    }
}
