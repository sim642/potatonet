package ee.potatonet.banklink.properties;

import java.security.PrivateKey;

import ee.potatonet.banklink.AbstractBanklink;

public class PropertiesBanklink extends AbstractBanklink {

  private String keyAlias;
  private String keyPassword;

  private PrivateKey privateKey;

  public String getKeyAlias() {
    return keyAlias;
  }

  public void setKeyAlias(String keyAlias) {
    this.keyAlias = keyAlias;
  }

  public String getKeyPassword() {
    return keyPassword;
  }

  public void setKeyPassword(String keyPassword) {
    this.keyPassword = keyPassword;
  }

  @Override
  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }
}
