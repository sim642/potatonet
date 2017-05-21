package ee.potatonet.configuration;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ee.potatonet.AppProperties;
import ee.potatonet.security.SslKeyStore;
import ee.potatonet.security.SslTrustStore;

@Configuration
public class ConnectorConfiguration {

  private final AppProperties appProperties;

  private final SslKeyStore sslKeyStore;
  private final SslTrustStore sslTrustStore;

  @Autowired
  public ConnectorConfiguration(AppProperties appProperties, SslKeyStore sslKeyStore, SslTrustStore sslTrustStore) {
    this.appProperties = appProperties;
    this.sslKeyStore = sslKeyStore;
    this.sslTrustStore = sslTrustStore;
  }

  @Bean
  public EmbeddedServletContainerFactory servletContainer() {
    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
      @Override
      protected void postProcessContext(Context context) {
        SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint("CONFIDENTIAL");
        SecurityCollection collection = new SecurityCollection();
        collection.addPattern("/*");
        securityConstraint.addCollection(collection);
        context.addConstraint(securityConstraint);
      }

      @Override
      protected void customizeConnector(Connector connector) {
        super.customizeConnector(connector);
        connector.setRedirectPort(appProperties.getDomainPort());
        connector.setParseBodyMethods("POST,PUT,DELETE");
      }
    };
    tomcat.addAdditionalTomcatConnectors(httpsConnector());
    tomcat.addAdditionalTomcatConnectors(httpsEidConnector());
    return tomcat;
  }

  private Http11NioConnector httpsBaseConnector() {
    Http11NioConnector connector = new Http11NioConnector();
    connector.setScheme("https");
    connector.setSecure(true);
    connector.setParseBodyMethods("POST,PUT,DELETE");

    Http11NioProtocol protocol = connector.getProtocolHandler();
    protocol.setSSLEnabled(true);
    protocol.setKeystoreFile(sslKeyStore.getKeyStoreFile());
    protocol.setKeystorePass(sslKeyStore.getKeyStorePassword());
    protocol.setKeyAlias(sslKeyStore.getKeyAlias());
    protocol.setKeyPass(sslKeyStore.getKeyPassword());

    return connector;
  }

  private Connector httpsConnector() {
    Http11NioConnector connector = httpsBaseConnector();
    connector.setPort(8443);

    return connector;
  }

  private Connector httpsEidConnector() {
    Http11NioConnector connector = httpsBaseConnector();
    connector.setPort(8446);

    Http11NioProtocol protocol = connector.getProtocolHandler();
    protocol.setTruststoreFile(sslTrustStore.getTrustStoreFile());
    protocol.setTruststorePass(sslTrustStore.getTrustStorePassword());
    protocol.setClientAuth("true");

    return connector;
  }

  private static class Http11NioConnector extends Connector {
    public Http11NioConnector() {
      super("org.apache.coyote.http11.Http11NioProtocol");
    }

    @Override
    public Http11NioProtocol getProtocolHandler() {
      return (Http11NioProtocol) super.getProtocolHandler();
    }
  }
}
