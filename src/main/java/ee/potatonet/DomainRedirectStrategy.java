package ee.potatonet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Component;

@Component
public class DomainRedirectStrategy extends DefaultRedirectStrategy {
  @Value("${app.domain}")
  private String domain;

  @Override
  protected String calculateRedirectUrl(String contextPath, String url) {
    return super.calculateRedirectUrl(domain + contextPath, url);
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }
}
