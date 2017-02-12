package ee.potatonet.eid;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class EIDCodeDetails {
  public enum Gender {
    MALE,
    FEMALE
  }

  private final String idCode;
  private final Gender gender;
  private final ZonedDateTime birthDate;

  public EIDCodeDetails(String idCode) {
    this.idCode = idCode;

    int genderDigit = Character.digit(idCode.charAt(0), 10);

    this.gender = genderDigit % 2 == 0 ? Gender.FEMALE : Gender.MALE;
    this.birthDate = ZonedDateTime.of(
        LocalDate.of(
            1800 + 100 * ((genderDigit - 1) / 2) + Integer.parseUnsignedInt(idCode.substring(1, 3)),
            Integer.parseUnsignedInt(idCode.substring(3, 5)),
            Integer.parseUnsignedInt(idCode.substring(5, 7))),
        LocalTime.NOON,
        ZoneId.of("Europe/Tallinn"));
  }

  public String getIdCode() {
    return idCode;
  }

  public Gender getGender() {
    return gender;
  }

  public ZonedDateTime getBirthDate() {
    return birthDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EIDCodeDetails that = (EIDCodeDetails) o;
    return Objects.equals(idCode, that.idCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idCode);
  }

  @Override
  public String toString() {
    return "EIDCodeDetails{" +
        "idCode='" + idCode + '\'' +
        ", gender=" + gender +
        ", birthDate=" + birthDate +
        '}';
  }
}
