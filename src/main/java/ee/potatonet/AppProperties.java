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

  private String trustStore;
  private String trustStorePassword;

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
}
