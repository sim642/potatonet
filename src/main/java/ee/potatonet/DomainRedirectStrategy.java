package ee.potatonet;

import org.springframework.security.web.DefaultRedirectStrategy;

public class DomainRedirectStrategy extends DefaultRedirectStrategy {
  private String domain;

  public DomainRedirectStrategy(String domain) {
    this.domain = domain;
  }

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
