package ee.potatonet.cucumber.steps;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import ee.potatonet.cucumber.config.CucumberTestState;
import ee.potatonet.cucumber.config.SpringCucumberSteps;
import ee.potatonet.data.model.Post;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.PostService;
import ee.potatonet.data.service.UserService;

@SpringCucumberSteps
public class PostStepDefs {

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @Autowired
  private CucumberTestState testState;

  @Given("^user \"([^\"]*)\" has a post with content \"([^\"]*)\"$")
  public void userHasAPostWithContent(String userEmail, String postContent) throws Throwable {
    User user = userService.findOneByEidEmail(userEmail);
    postService.savePostToUser(new Post(null, postContent), user);
  }

  @Given("^my friend has a post with content \"([^\"]*)\"")
  public void myFriendHasAPostWithContentAndId(String postContent) throws Throwable {
    User friend = testState.getFriend();
    postService.savePostToUser(new Post(null, postContent), friend);
  }
}
