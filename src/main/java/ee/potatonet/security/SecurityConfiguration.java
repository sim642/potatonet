package ee.potatonet.security;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {

  static {
    Security.addProvider(new BouncyCastleProvider());
    CryptoRestrictions.removeRestrictions();
  }

}
