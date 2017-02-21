package ee.potatonet.eid;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Embeddable
public class EIDCodeDetails {
  public enum Gender {
    MALE,
    FEMALE
  }

  private String idCode;
  @Enumerated(EnumType.STRING)
  private Gender gender;
  // @Temporal(TemporalType.DATE)
  private LocalDate birthDate;

  private EIDCodeDetails() {

  }

  public EIDCodeDetails(String idCode) {
    this.idCode = idCode;
    extractDetails();
  }

  private void extractDetails() {
    int genderDigit = Character.digit(idCode.charAt(0), 10);

    gender = genderDigit % 2 == 0 ? Gender.FEMALE : Gender.MALE;
    birthDate = LocalDate.of(
        1800 + 100 * ((genderDigit - 1) / 2) + Integer.parseUnsignedInt(idCode.substring(1, 3)),
        Integer.parseUnsignedInt(idCode.substring(3, 5)),
        Integer.parseUnsignedInt(idCode.substring(5, 7)));
  }

  public String getIdCode() {
    return idCode;
  }

  public Gender getGender() {
    return gender;
  }

  public LocalDate getBirthDate() {
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
