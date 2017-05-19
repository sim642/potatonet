package ee.potatonet.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface SslTrustStore {
  String getTrustStoreFile();
  String getTrustStorePassword();
}
