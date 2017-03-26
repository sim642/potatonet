package ee.potatonet.configuration;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ee.potatonet.AppProperties;

@Configuration
public class ConnectorConfiguration {

  private final AppProperties appProperties;
  private final ServerProperties serverProperties;

  @Autowired
  public ConnectorConfiguration(AppProperties appProperties, ServerProperties serverProperties) {
    this.appProperties = appProperties;
    this.serverProperties = serverProperties;
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
        connector.setParseBodyMethods("POST,PUT,DELETE");
      }
    };
    tomcat.addAdditionalTomcatConnectors(httpConnector());
    tomcat.addAdditionalTomcatConnectors(httpsEidConnector());
    return tomcat;
  }

  // Redirects requests to HTTP port 8080 to HTTPS port
  private Connector httpConnector() {
    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
    connector.setScheme("http");
    connector.setPort(8080);
    connector.setRedirectPort(appProperties.getDomainPort());
    return connector;
  }

  private Connector httpsEidConnector() {
    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
    Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
    connector.setScheme("https");
    connector.setSecure(true);
    connector.setPort(8446);

    protocol.setSSLEnabled(true);
    Ssl ssl = serverProperties.getSsl();
    protocol.setKeystoreFile(ssl.getKeyStore());
    protocol.setKeystorePass(ssl.getKeyStorePassword());
    protocol.setKeyPass(ssl.getKeyPassword());

    protocol.setTruststoreFile(appProperties.getTrustStore());
    protocol.setTruststorePass(appProperties.getTrustStorePassword());
    protocol.setClientAuth("true");

    return connector;
  }
}
