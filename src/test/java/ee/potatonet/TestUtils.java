package ee.potatonet;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ee.potatonet.auth.eid.EIDCodeDetails;
import ee.potatonet.auth.eid.EIDDetails;
import ee.potatonet.data.model.User;

public class TestUtils {
  private static final AtomicInteger userNameCount = new AtomicInteger(0);

  public static User generateUser() {
    String generatedName = "generated_user_" + userNameCount.incrementAndGet();
    User user = new User(new EIDDetails(new EIDCodeDetails("30101010000"), generatedName, generatedName, generatedName));
    user.setPassword(new BCryptPasswordEncoder().encode("password"));
    return user;
  }
}
