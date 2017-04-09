package ee.potatonet.cucumber.steps;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
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
  private WebDriver webDriver;

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

  @When("^I create a post with content \"([^\"]*)\"$")
  public void iCreateAPostWithContent(String postContent) throws Throwable {
    WebElement postTextArea = webDriver.findElement(
        By.xpath("//form[@id='post']//textarea")
    );

    WebElement postButton = webDriver.findElement(
        By.xpath("//form[@id='post']//button[@id='postButton']")
    );

    postTextArea.sendKeys(postContent);
    postButton.submit();
  }

  @When("^user \"([^\"]*)\" likes a post with content \"([^\"]*)\"$")
  public void userLikesAPostWithContent(String userEmail, String postContent) throws Throwable {
    User user = userService.findOneByEidEmail(userEmail);
    List<Post> posts = postService.getUserFeedPosts(user, null);
    Optional<Post> post = posts.stream()
        .filter(p -> p.getContent().equals(postContent))
        .findAny();

    assertTrue(post.isPresent());

    postService.toggleLike(user, post.get().getId());
  }
}
