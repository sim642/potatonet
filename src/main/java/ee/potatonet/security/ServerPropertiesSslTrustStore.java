package ee.potatonet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
@ConditionalOnProperty("server.ssl.trust-store")
public class ServerPropertiesSslTrustStore implements SslTrustStore {

  private final Ssl sslProperties;

  @Autowired
  public ServerPropertiesSslTrustStore(ServerProperties serverProperties) {
    this.sslProperties = serverProperties.getSsl();
  }

  @Override
  public String getTrustStoreFile() {
    return sslProperties.getTrustStore();
  }

  @Override
  public String getTrustStorePassword() {
    return sslProperties.getTrustStorePassword();
  }
}
