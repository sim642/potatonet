package ee.potatonet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
@ConditionalOnProperty("server.ssl.key-store")
public class ServerPropertiesSslKeyStore implements SslKeyStore {

  private final Ssl sslProperties;

  @Autowired
  public ServerPropertiesSslKeyStore(ServerProperties serverProperties) {
    sslProperties = serverProperties.getSsl();
  }

  @Override
  public String getKeyStoreFile() {
    return sslProperties.getKeyStore();
  }

  @Override
  public String getKeyStorePassword() {
    return sslProperties.getKeyStorePassword();
  }

  @Override
  public String getKeyAlias() {
    return sslProperties.getKeyAlias();
  }

  @Override
  public String getKeyPassword() {
    return sslProperties.getKeyPassword();
  }
}
