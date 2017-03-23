package ee.potatonet.data;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.potatonet.TemplateRenderService;
import ee.potatonet.data.repos.PostRepository;

@Service
@Transactional
public class PostService {

  private static final int FEED_COUNT = 15;

  private final PostRepository postRepository;
  private final UserService userService;
  private final TemplateRenderService templateRenderService;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  public PostService(PostRepository postRepository, UserService userService, TemplateRenderService templateRenderService, SimpMessagingTemplate simpMessagingTemplate) {
    this.postRepository = postRepository;
    this.userService = userService;
    this.templateRenderService = templateRenderService;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  public Post findById(Long id) {
    return postRepository.findOne(id);
  }

  public Post find(Post post) {
    return findById(post.getId());
  }

  public List<Post> getUserFeedPosts(User user, Post beforePost) {
    user = userService.find(user);

    ZonedDateTime beforeDateTime = beforePost != null ? beforePost.getCreationDateTime() : ZonedDateTime.now();
    List<Post> feedPosts = postRepository.findAllFeedPosts(user, beforeDateTime, FEED_COUNT);

    return feedPosts;
  }

  public List<Post> getUserProfilePosts(User user, Post beforePost) {
    user = userService.find(user);

    ZonedDateTime beforeDateTime = beforePost != null ? beforePost.getCreationDateTime() : ZonedDateTime.now();
    List<Post> userFeedPosts = postRepository.findAllUserFeedPosts(user, beforeDateTime, FEED_COUNT);

    return userFeedPosts;
  }

  public void savePostToUser(Post post, User user) {
    user = userService.find(user);

    post.setUser(user);
    post.setCreationDateTime(ZonedDateTime.now());
    post = postRepository.save(post);

    user.getPosts().add(post);
    userService.save(user);

    String output = templateRenderService.render("common", Collections.singleton("postPanel"), Collections.singletonMap("post", post));
    simpMessagingTemplate.convertAndSend("/topic/posts/" + user.getId(), output);
  }

  public List<Coordinates> getAllPostCoordinates() {
    return postRepository.findAllPostCoordinates();
  }
}