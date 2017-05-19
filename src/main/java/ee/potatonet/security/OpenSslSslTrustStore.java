package ee.potatonet.security;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import ee.potatonet.AppProperties;

@Configuration
@ConditionalOnProperty("app.eid-root-certificate-pattern")
public class OpenSslSslTrustStore implements SslTrustStore {

  private static final String PASSWORD = "123456";

  private final Path trustStorePath;

  @Autowired
  public OpenSslSslTrustStore(AppProperties appProperties) {
    try {
      PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource[] resources = resourceResolver.getResources(appProperties.getEidRootCertificatePattern());

      Map<String, Certificate> namedCertificates = new HashMap<>();
      for (Resource resource : resources) {
        String name = FilenameUtils.removeExtension(resource.getFilename());
        Certificate certificate = OpenSslUtils.readCertificate(new InputStreamReader(resource.getInputStream()));
        namedCertificates.put(name, certificate);
      }

      trustStorePath = KeyStoreUtils.newTemporaryTrustStore(PASSWORD.toCharArray(), namedCertificates);
    }
    catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public String getTrustStoreFile() {
    return "file:" + trustStorePath;
  }

  @Override
  public String getTrustStorePassword() {
    return PASSWORD;
  }
}
