package ee.potatonet.banklink;

import java.security.PrivateKey;

public interface Banklink {
  String getDisplayName();

  String getClientId();

  String getUrl();

  String getAccountNumber();

  String getAccountName();

  PrivateKey getPrivateKey();
}
