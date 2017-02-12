package ee.potatonet.eid;

import java.security.cert.X509Certificate;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.security.web.authentication.preauth.x509.SubjectDnX509PrincipalExtractor;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;

public class EIDDetailsX509PrincipalExtractor implements X509PrincipalExtractor {
  private final SubjectDnX509PrincipalExtractor idCodeExtractor;
  private final SubjectDnX509PrincipalExtractor givenNameExtractor;
  private final SubjectDnX509PrincipalExtractor surnameExtractor;
  private final EIDEmailX509PrincipalExtractor emailExtractor;

  public EIDDetailsX509PrincipalExtractor() {
    idCodeExtractor = new SubjectDnX509PrincipalExtractor();
    idCodeExtractor.setSubjectDnRegex("serialNumber=(\\d{11})");

    givenNameExtractor = new SubjectDnX509PrincipalExtractor();
    givenNameExtractor.setSubjectDnRegex("givenName=(.*?)(?:,|$)");

    surnameExtractor = new SubjectDnX509PrincipalExtractor();
    surnameExtractor.setSubjectDnRegex("surName=(.*?)(?:,|$)");

    emailExtractor = new EIDEmailX509PrincipalExtractor();
  }

  @Override
  public EIDDetails extractPrincipal(X509Certificate cert) {
    String idCode = (String) idCodeExtractor.extractPrincipal(cert);
    String givenName = capitalize((String) givenNameExtractor.extractPrincipal(cert));
    String surname = capitalize((String) surnameExtractor.extractPrincipal(cert));
    String email = emailExtractor.extractPrincipal(cert);

    return new EIDDetails(idCode, givenName, surname, email);
  }

  private static String capitalize(String name) {
    return WordUtils.capitalizeFully(name, ' ', '-');
  }
}
