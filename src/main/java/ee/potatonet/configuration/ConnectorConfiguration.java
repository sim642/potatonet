package ee.potatonet.configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import ee.potatonet.AppProperties;
import ee.potatonet.security.OpenSslUtils;

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

    String password = appProperties.getOpensslCertificateKeyPassword();
    try {
      char[] rawPassword = password.toCharArray();

      PrivateKey privateKey = OpenSslUtils.readPrivateKey(new InputStreamReader(ResourceUtils.getURL(appProperties.getOpensslCertificateKey()).openStream()), rawPassword);
      List<Certificate> certificates = OpenSslUtils.readCertificates(new InputStreamReader(ResourceUtils.getURL(appProperties.getOpensslCertificateFullChain()).openStream()));

      try {
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(null, rawPassword);

        Certificate[] fullChain = certificates.toArray(new Certificate[0]);
        keyStore.setKeyEntry("ssl", privateKey, rawPassword, fullChain);

        try (OutputStream os = Files.newOutputStream(Paths.get("testkeystore.jks"))) {
          keyStore.store(os, rawPassword);
        }
      }
      catch (KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    }
    catch (IOException e) {
      throw new UncheckedIOException(e);
    }


    Http11NioProtocol protocol = connector.getProtocolHandler();
    protocol.setSSLEnabled(true);
    /*Ssl ssl = serverProperties.getSsl();
    protocol.setKeystoreFile(ssl.getKeyStore());
    protocol.setKeystorePass(ssl.getKeyStorePassword());
    protocol.setKeyPass(ssl.getKeyPassword());*/
    protocol.setKeystoreFile("file:./testkeystore.jks");
    protocol.setKeystorePass(password);
    protocol.setKeyPass(password);


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
    protocol.setTruststoreFile(appProperties.getTrustStore());
    protocol.setTruststorePass(appProperties.getTrustStorePassword());
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
