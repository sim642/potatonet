package ee.potatonet.banklink;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface Banklink {
  String getDisplayName();

  String getClientId();

  String getUrl();

  String getAccountNumber();

  String getAccountName();

  PrivateKey getPrivateKey();

  Certificate getBankCertificate();
}
