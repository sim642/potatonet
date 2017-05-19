package ee.potatonet.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface SslKeyStore {
  String getKeyStoreFile();
  String getKeyStorePassword();
  String getKeyAlias();
  String getKeyPassword();
}
