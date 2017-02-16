package ee.potatonet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
  private boolean showErrors;

  private String domain;
  private String domainEid;

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

  public String getDomainEid() {
    return domainEid;
  }

  public void setDomainEid(String domainEid) {
    this.domainEid = domainEid;
  }
}
