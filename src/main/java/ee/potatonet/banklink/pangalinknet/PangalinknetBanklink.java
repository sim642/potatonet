package ee.potatonet.banklink.pangalinknet;

import java.security.PrivateKey;
import java.security.cert.Certificate;

import ee.potatonet.banklink.AbstractBanklink;

public class PangalinknetBanklink extends AbstractBanklink {

  private PrivateKey privateKey;
  private Certificate bankCertificate;

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
