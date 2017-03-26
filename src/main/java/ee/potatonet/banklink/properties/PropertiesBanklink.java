package ee.potatonet.banklink.properties;

import java.security.PrivateKey;
import java.security.cert.Certificate;

import ee.potatonet.banklink.AbstractBanklink;

public class PropertiesBanklink extends AbstractBanklink {

  private String keyAlias;
  private String keyPassword;
  private String certificateAlias;

  private PrivateKey privateKey;
  private Certificate bankCertificate;

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

  public String getCertificateAlias() {
    return certificateAlias;
  }

  public void setCertificateAlias(String certificateAlias) {
    this.certificateAlias = certificateAlias;
  }

  @Override
  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }

  @Override
  public Certificate getBankCertificate() {
    return bankCertificate;
  }

  public void setBankCertificate(Certificate bankCertificate) {
    this.bankCertificate = bankCertificate;
  }
}
