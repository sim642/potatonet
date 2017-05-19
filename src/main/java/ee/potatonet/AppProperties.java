package ee.potatonet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
  private boolean showErrors;

  private String domain;
  private String domainPort;
  private String domainEid;

  private String opensslCertificateFullChain;
  private String opensslCertificateKey;
  private String opensslCertificateKeyPassword;

  private String eidRootCertificatePattern;

  private String trustStore;
  private String trustStorePassword;

  private String googleApiKey;

  public boolean getShowErrors() {
    return showErrors;
  }

  public void setShowErrors(boolean showErrors) {
    this.showErrors = showErrors;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public int getDomainPort() {
    return Integer.valueOf(domainPort);
  }

  public void setDomainPort(int domainPort) {
    this.domainPort = String.valueOf(domainPort);
  }

  public String getDomainEid() {
    return domainEid;
  }

  public void setDomainEid(String domainEid) {
    this.domainEid = domainEid;
  }

  public String getOpensslCertificateFullChain() {
    return opensslCertificateFullChain;
  }

  public void setOpensslCertificateFullChain(String opensslCertificateFullChain) {
    this.opensslCertificateFullChain = opensslCertificateFullChain;
  }

  public String getOpensslCertificateKey() {
    return opensslCertificateKey;
  }

  public void setOpensslCertificateKey(String opensslCertificateKey) {
    this.opensslCertificateKey = opensslCertificateKey;
  }

  public String getOpensslCertificateKeyPassword() {
    return opensslCertificateKeyPassword;
  }

  public void setOpensslCertificateKeyPassword(String opensslCertificateKeyPassword) {
    this.opensslCertificateKeyPassword = opensslCertificateKeyPassword;
  }

  public String getEidRootCertificatePattern() {
    return eidRootCertificatePattern;
  }

  public void setEidRootCertificatePattern(String eidRootCertificatePattern) {
    this.eidRootCertificatePattern = eidRootCertificatePattern;
  }

  public String getTrustStore() {
    return trustStore;
  }

  public void setTrustStore(String trustStore) {
    this.trustStore = trustStore;
  }

  public String getTrustStorePassword() {
    return trustStorePassword;
  }

  public void setTrustStorePassword(String trustStorePassword) {
    this.trustStorePassword = trustStorePassword;
  }

  public String getGoogleApiKey() {
    return googleApiKey;
  }

  public void setGoogleApiKey(String googleApiKey) {
    this.googleApiKey = googleApiKey;
  }
}
