package ee.potatonet.cucumber.config;

import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Component
public class CucumberTestContext {

  private HtmlPage currentPage;

  public HtmlPage getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(HtmlPage currentPage) {
    this.currentPage = currentPage;
  }
}
