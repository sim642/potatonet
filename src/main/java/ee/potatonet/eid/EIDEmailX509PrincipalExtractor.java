package ee.potatonet.eid;

import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;

import sun.security.x509.RFC822Name;

public class EIDEmailX509PrincipalExtractor implements X509PrincipalExtractor {
  @Override
  public String extractPrincipal(X509Certificate cert) {
    try {
      return new RFC822Name((String) cert.getSubjectAlternativeNames().iterator().next().get(1)).getName();
    }
    catch (CertificateParsingException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
