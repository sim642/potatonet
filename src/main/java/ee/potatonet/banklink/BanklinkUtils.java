package ee.potatonet.banklink;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public final class BanklinkUtils {

  private BanklinkUtils() {

  }

  public static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(DateTimeFormatter.ISO_LOCAL_DATE)
      .appendLiteral('T')
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(MINUTE_OF_HOUR, 2)
      .appendLiteral(':')
      .appendValue(SECOND_OF_MINUTE, 2)
      .appendPattern("xx")
      .parseStrict()
      .toFormatter();

  // http://www.pangaliit.ee/et/arveldused/viitenumber
  public static String getReferenceNumber(String stamp) {
    return stamp + get731Checksum(stamp);
  }

  private static int get731Checksum(String number) {
    int[] weights = {7, 3, 1};
    int sum = 0;
    for (int i = number.length() - 1, j = 0; i >= 0; i--, j++) {
      sum += Character.digit(number.charAt(i), 10) * weights[j % weights.length];
    }
    return ((int) Math.ceil(sum / 10.0)) * 10 - sum;
  }
}
