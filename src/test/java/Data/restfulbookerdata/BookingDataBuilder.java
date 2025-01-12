package Data.restfulbookerdata;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import net.datafaker.Faker;

public class BookingDataBuilder {

    private static final Faker FAKER = new Faker ();

    public static BookingData getBookingData () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate checkinDate = LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(1, 21));
        LocalDate checkoutDate = LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(1, 6));

        return BookingData.builder()
                .firstname(FAKER.name().firstName())
                .lastname(FAKER.name().lastName())
                .totalprice(FAKER.number().numberBetween(1, 2000))
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(checkinDate.format(formatter))
                        .checkout(checkoutDate.format(formatter))
                        .build())
                .additionalneeds("Breakfast")
                .build();

    }

    public static PartialBookingData getPartialBookingData () {
        return PartialBookingData.builder ()
                .firstname (FAKER.name ()
                        .firstName ())
                .totalprice (FAKER.number ()
                        .numberBetween (100, 5000))
                .build ();
    }
}