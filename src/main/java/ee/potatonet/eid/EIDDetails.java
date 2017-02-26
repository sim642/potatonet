package ee.potatonet.eid;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.Objects;

@Embeddable
public class EIDDetails {
  @Embedded
  private EIDCodeDetails eidCode;
  private String givenName;
  private String surname;
  private String email;

  private EIDDetails() {

  }

  public EIDDetails(EIDCodeDetails eidCode, String givenName, String surname, String email) {
    this.eidCode = eidCode;
    this.givenName = givenName;
    this.surname = surname;
    this.email = email;
  }

  public EIDCodeDetails getEidCode() {
    return eidCode;
  }

  public String getGivenName() {
    return givenName;
  }

  public String getSurname() {
    return surname;
  }

  public String getEmail() {
    return email;
  }

  public String getFullName() {
    return givenName + " " + surname;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EIDDetails that = (EIDDetails) o;
    return Objects.equals(eidCode, that.eidCode) &&
        Objects.equals(givenName, that.givenName) &&
        Objects.equals(surname, that.surname) &&
        Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eidCode, givenName, surname, email);
  }

  @Override
  public String toString() {
    return "EIDDetails{" +
        "eidCode=" + eidCode +
        ", givenName='" + givenName + '\'' +
        ", surname='" + surname + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
