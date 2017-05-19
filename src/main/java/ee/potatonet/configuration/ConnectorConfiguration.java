package ee.potatonet.configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.commons.io.FilenameUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import ee.potatonet.AppProperties;
import ee.potatonet.security.KeyStoreUtils;
import ee.potatonet.security.OpenSslUtils;
import ee.potatonet.security.SslKeyStore;

@Configuration
public class ConnectorConfiguration {

  private final AppProperties appProperties;
  private final ServerProperties serverProperties;

  @Autowired
  public SslKeyStore sslKeyStore;

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

    String password = "123456";
    Path trustStorePath;
    try {
      PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource[] resources = resourceResolver.getResources(appProperties.getEidRootCertificatePattern());

      Map<String, Certificate> namedCertificates = new HashMap<>();
      for (Resource resource : resources) {
        String name = FilenameUtils.removeExtension(resource.getFilename());
        Certificate certificate = OpenSslUtils.readCertificate(new InputStreamReader(resource.getInputStream()));
        namedCertificates.put(name, certificate);
      }

      trustStorePath = KeyStoreUtils.newTemporaryTrustStore(password.toCharArray(), namedCertificates);
    }
    catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    Http11NioProtocol protocol = connector.getProtocolHandler();
    /*protocol.setTruststoreFile(appProperties.getTrustStore());
    protocol.setTruststorePass(appProperties.getTrustStorePassword());*/
    protocol.setTruststoreFile("file:" + trustStorePath);
    protocol.setTruststorePass(password);
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
