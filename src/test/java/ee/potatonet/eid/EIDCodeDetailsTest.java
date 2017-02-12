package ee.potatonet.eid;

import static org.junit.Assert.*;

import java.time.LocalDate;
import org.junit.Test;

public class EIDCodeDetailsTest {
  @Test
  public void testWiki1() throws Exception {
    EIDCodeDetails details = new EIDCodeDetails("34501234215");

    assertEquals(EIDCodeDetails.Gender.MALE, details.getGender());
    assertEquals(LocalDate.of(1945, 1, 23), details.getBirthDate().toLocalDate());
  }

  @Test
  public void testWiki2a() throws Exception {
    EIDCodeDetails details = new EIDCodeDetails("49403136515");

    assertEquals(EIDCodeDetails.Gender.FEMALE, details.getGender());
    assertEquals(LocalDate.of(1994, 3, 13), details.getBirthDate().toLocalDate());
  }

  @Test
  public void testWiki2b() throws Exception {
    EIDCodeDetails details = new EIDCodeDetails("49403136526");

    assertEquals(EIDCodeDetails.Gender.FEMALE, details.getGender());
    assertEquals(LocalDate.of(1994, 3, 13), details.getBirthDate().toLocalDate());
  }
}