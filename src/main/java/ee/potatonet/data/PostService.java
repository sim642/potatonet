package ee.potatonet.data;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.potatonet.TemplateRenderService;
import ee.potatonet.data.repos.PostRepository;

@Service
@Transactional
public class PostService {

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

  public List<Post> getUserFeedPosts(User user) {
    user = userService.find(user);

    List<Post> friendsPosts = postRepository.findAllPostsFromFriends(user);
    List<Post> usersPosts = user.getPosts();

    return Stream.concat(friendsPosts.stream(), usersPosts.stream())
        .sorted(Comparator.comparing(Post::getCreationDateTime).reversed())
        .collect(Collectors.toList());
  }

  public List<Post> getUserProfilePosts(User user) {
    user = userService.find(user);

    return user.getPosts().stream()
        .sorted(Comparator.comparing(Post::getCreationDateTime).reversed())
        .collect(Collectors.toList());
  }

  public void savePostToUser(Post post, User user) {
    user = userService.find(user);

    post.setUser(user);
    post.setCreationDateTime(ZonedDateTime.now());
    post = postRepository.save(post);

    user.getPosts().add(post);
    userService.save(user);

    String output = templateRenderService.render("common", Collections.singleton("post"), Collections.singletonMap("postInfo", post));
    simpMessagingTemplate.convertAndSend("/topic/posts/" + user.getId(), output);
  }
}