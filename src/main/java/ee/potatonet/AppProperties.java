package ee.potatonet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
  private boolean showErrors;

  private String domain;

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
}
