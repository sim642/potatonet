package ee.potatonet.cucumber.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import ee.potatonet.cucumber.config.CucumberTestState;
import ee.potatonet.cucumber.config.SpringCucumberSteps;
import ee.potatonet.data.model.Comment;
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

//  @When("^I add a comment \"([^\"]*)\" to post \"([^\"]*)\"$")
//  public void iAddACommentToPost(String commentText, String postText) throws Throwable {
//    try {
//      WebElement commentExpandButton = new WebDriverWait(webDriver, 2).until(
//          ExpectedConditions.elementToBeClickable(
//              By.xpath("//div[contains(@class, 'panel-post')]//div[contains(@class, 'media-body')][.//p[text()='" + postText + "']]" +
//                  "//ul[contains(@class, 'list-comments')]//button[contains(@class, 'btn-comment-expand')]")
//          )
//      );
//      commentExpandButton.click();
//
//      WebElement commentTextArea = new WebDriverWait(webDriver, 2).until(
//          ExpectedConditions.presenceOfElementLocated(
//              By.xpath("//div[contains(@class, 'panel-post')]//div[contains(@class, 'media-body')][.//p[text()='" + postText + "']]" +
//                  "//ul[contains(@class, 'list-comments')]//textarea")
//          )
//      );
//      commentTextArea.sendKeys(Keys.TAB);
//      commentTextArea.clear();
//      commentTextArea.sendKeys(commentText);
//
//      By submitButtonXpath = By.xpath("//div[contains(@class, 'panel-post')]//div[contains(@class, 'media-body')][.//p[text()='" + postText + "']]" +
//          "//ul[contains(@class, 'list-comments')]//button[@type='submit'][contains(@class, 'btn-comment')]");
//
//      Boolean buttonIsReady = new WebDriverWait(webDriver, 2).until(
//          ExpectedConditions.and(
//              ExpectedConditions.presenceOfElementLocated(submitButtonXpath),
//              ExpectedConditions.elementToBeClickable(submitButtonXpath)
//          )
//      );
//
//      if (!buttonIsReady) {
//        fail();
//      }
//
//      WebElement commentSubmitButton = webDriver.findElement(submitButtonXpath);
//      ((JavascriptExecutor) webDriver).executeScript("arguments[0].disabled = false; arguments[0].click();", commentSubmitButton);
//
//      commentSubmitButton.submit();
//    }
//    catch (TimeoutException e) {
//      fail();
//    }
//  }

  @Given("^my friend has a comment \"([^\"]*)\" for post with content \"([^\"]*)\"$")
  public void myFriendHasACommentForPostWithContent(String commentContent, String postContent) throws Throwable {
    User friend = testState.getFriend();

    Optional<Post> post = postService.getUserFeedPosts(friend, null).stream()
        .filter(p -> p.getContent().equals(postContent))
        .findAny();

    if (!post.isPresent()) {
      fail();
    }

    postService.saveCommentToPost(new Comment(post.get(), friend, commentContent), post.get());
  }

  @Given("^I have a comment \"([^\"]*)\" for post with content \"([^\"]*)\"$")
  public void iHaveACommentForPostWithContent(String commentContent, String postContent) throws Throwable {
    User me = testState.getMyUser();

    Optional<Post> post = postService.getUserFeedPosts(me, null).stream()
        .filter(p -> p.getContent().equals(postContent))
        .findAny();

    if (!post.isPresent()) {
      fail();
    }

    postService.saveCommentToPost(new Comment(post.get(), me, commentContent), post.get());
  }
}
