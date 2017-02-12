package ee.potatonet.eid;

import java.util.Objects;

public class EIDDetails {
  private String idCode;
  private String givenName;
  private String surname;
  private String email;

  public EIDDetails(String idCode, String givenName, String surname, String email) {
    this.idCode = idCode;
    this.givenName = givenName;
    this.surname = surname;
    this.email = email;
  }

  public String getIdCode() {
    return idCode;
  }

  public void setIdCode(String idCode) {
    this.idCode = idCode;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
    return Objects.equals(idCode, that.idCode) &&
        Objects.equals(givenName, that.givenName) &&
        Objects.equals(surname, that.surname) &&
        Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idCode, givenName, surname, email);
  }

  @Override
  public String toString() {
    return "EIDDetails{" +
        "idCode='" + idCode + '\'' +
        ", givenName='" + givenName + '\'' +
        ", surname='" + surname + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
