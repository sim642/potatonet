package ee.potatonet.security;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import ee.potatonet.AppProperties;

@Configuration
@ConditionalOnProperty("app.openssl-certificate-key")
public class OpenSslSslKeyStore implements SslKeyStore {

  private static final String ALIAS = "privatekey";
  private static final String DEFAULT_PASSWORD = "123456";

  private final Path keyStorePath;
  private final String password;

  @Autowired
  public OpenSslSslKeyStore(AppProperties appProperties) {
    String definedPassword = appProperties.getOpensslCertificateKeyPassword();
    password = definedPassword != null ? definedPassword : DEFAULT_PASSWORD; // JKS must have a password

    try {
      char[] rawPassword = definedPassword != null ? definedPassword.toCharArray() : null; // OpenSSL key might not have a password

      PrivateKey privateKey = OpenSslUtils.readPrivateKey(new InputStreamReader(ResourceUtils.getURL(appProperties.getOpensslCertificateKey()).openStream()), rawPassword);
      List<Certificate> certificates = OpenSslUtils.readCertificates(new InputStreamReader(ResourceUtils.getURL(appProperties.getOpensslCertificateFullChain()).openStream()));

      keyStorePath = KeyStoreUtils.newTemporaryKeyStore(ALIAS, password.toCharArray(), privateKey, certificates);
    }
    catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public String getKeyStoreFile() {
    return "file:" + keyStorePath;
  }

  @Override
  public String getKeyStorePassword() {
    return password;
  }

  @Override
  public String getKeyAlias() {
    return ALIAS;
  }

  @Override
  public String getKeyPassword() {
    return password;
  }
}
