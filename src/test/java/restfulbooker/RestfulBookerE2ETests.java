package restfulbooker;

import static Data.restfulbookerdata.BookingDataBuilder.getBookingData;
import static Data.restfulbookerdata.BookingDataBuilder.getPartialBookingData;
import static Data.restfulbookerdata.TokenBuilder.getToken;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertNotNull;

import Data.restfulbookerdata.BookingData;
import Data.restfulbookerdata.PartialBookingData;
import Data.restfulbookerdata.Tokencreds;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Rest Assured POC - Example Tests")
@Feature("Writing End to End tests using rest-assured")
public class RestfulBookerE2ETests extends BaseSetup {

    private BookingData newBooking;
    private int bookingId;
    private String token;

    @BeforeClass
    public void testSetup() {
        newBooking = getBookingData();
    }

    @Test(priority = 1)
    @Description("Example test for fetching token value - Post request")
    @Severity(SeverityLevel.BLOCKER)
    @Story("End to End tests using rest-assured")
    @Step("Generate Token")
    public void testTokenGeneration() {
        Tokencreds tokenCreds = getToken();
        token = given()
                .body(tokenCreds)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .assertThat()
                .body("token", is(notNullValue()))
                .extract()
                .path("token").toString();

        assertNotNull(token, "Authentication token should not be null");
    }

    @Test(priority = 2)
    @Description("Example test for creating new booking - Post request")
    @Severity(SeverityLevel.BLOCKER)
    @Story("End to End tests using rest-assured")
    @Step("Create new booking")
    public void createBookingTest() {
        bookingId = given()
                .body(newBooking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo(newBooking.getFirstname()))
                .body("booking.lastname", equalTo(newBooking.getLastname()))
                .body("booking.totalprice", equalTo(newBooking.getTotalprice()))
                .body("booking.depositpaid", equalTo(newBooking.isDepositpaid()))
                .body("booking.bookingdates.checkin", equalTo(newBooking.getBookingdates().getCheckin()))
                .body("booking.bookingdates.checkout", equalTo(newBooking.getBookingdates().getCheckout()))
                .body("booking.additionalneeds", equalTo(newBooking.getAdditionalneeds()))
                .extract()
                .path("bookingid");

        assertNotNull(bookingId, "Booking ID should not be null");
    }

    @Test(priority = 3, dependsOnMethods = {"createBookingTest"})
    @Description("Example test for retrieving a booking - Get request")
    @Severity(SeverityLevel.CRITICAL)
    @Story("End to End tests using rest-assured")
    @Step("Get the newly created booking")
    public void getBookingTest() {
        given()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("firstname", equalTo(newBooking.getFirstname()))
                .body("lastname", equalTo(newBooking.getLastname()))
                .body("totalprice", equalTo(newBooking.getTotalprice()))
                .body("depositpaid", equalTo(newBooking.isDepositpaid()))
                .body("bookingdates.checkin", equalTo(newBooking.getBookingdates().getCheckin()))
                .body("bookingdates.checkout", equalTo(newBooking.getBookingdates().getCheckout()))
                .body("additionalneeds", equalTo(newBooking.getAdditionalneeds()));
    }

    @Test(priority = 4, dependsOnMethods = {"getBookingTest", "testTokenGeneration"})
    @Description("Example test for updating a booking - Put request")
    @Severity(SeverityLevel.NORMAL)
    @Story("End to End tests using rest-assured")
    @Step("Update the booking")
    public void updateBookingTest() {
        BookingData updatedBooking = getBookingData();
        given()
                .body(updatedBooking)
                .header("Cookie", "token=" + token)
                .when()
                .put("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("firstname", equalTo(updatedBooking.getFirstname()))
                .body("lastname", equalTo(updatedBooking.getLastname()))
                .body("totalprice", equalTo(updatedBooking.getTotalprice()))
                .body("depositpaid", equalTo(updatedBooking.isDepositpaid()))
                .body("bookingdates.checkin", equalTo(updatedBooking.getBookingdates().getCheckin()))
                .body("bookingdates.checkout", equalTo(updatedBooking.getBookingdates().getCheckout()))
                .body("additionalneeds", equalTo(updatedBooking.getAdditionalneeds()));
    }

    @Test(priority = 5, dependsOnMethods = {"updateBookingTest"})
    @Description("Example test for updating a booking partially - Patch request")
    @Severity(SeverityLevel.NORMAL)
    @Story("End to End tests using rest-assured")
    @Step("Update the booking partially")
    public void updatePartialBookingTest() {
        PartialBookingData partialUpdateBooking = getPartialBookingData();
        given()
                .body(partialUpdateBooking)
                .header("Cookie", "token=" + token)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("firstname", equalTo(partialUpdateBooking.getFirstname()))
                .body("totalprice", equalTo(partialUpdateBooking.getTotalprice()));
    }

    @Test(priority = 6, dependsOnMethods = {"updatePartialBookingTest"})
    @Description("Example test for deleting a booking - Delete request")
    @Severity(SeverityLevel.NORMAL)
    @Story("End to End tests using rest-assured")
    @Step("Delete the booking")
    public void deleteBookingTest() {
        given()
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(201);
    }

    @Test(priority = 7, dependsOnMethods = {"deleteBookingTest"})
    @Description("Example test for checking if booking is deleted by retrieving a deleted booking - Get request")
    @Severity(SeverityLevel.NORMAL)
    @Story("End to End tests using rest-assured")
    @Step("Check by retrieving deleted booking")
    public void checkBookingIsDeleted() {
        given()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(404);
    }
}