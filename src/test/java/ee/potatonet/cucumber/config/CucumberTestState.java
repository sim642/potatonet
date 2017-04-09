package ee.potatonet.cucumber.config;

import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import ee.potatonet.data.model.User;

@Component
public class CucumberTestState {

  private User friend;
  private User myUser;

  public User getFriend() {
    return friend;
  }

  public void setFriend(User friend) {
    this.friend = friend;
  }

  public User getMyUser() {
    return myUser;
  }

  public void setMyUser(User myUser) {
    this.myUser = myUser;
  }
}
