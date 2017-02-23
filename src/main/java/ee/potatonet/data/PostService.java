package ee.potatonet.data;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ee.potatonet.data.repos.PostRepository;

@Service
@Transactional
public class PostService {

  private final PostRepository postRepository;
  private final UserService userService;

  @Autowired
  public PostService(PostRepository postRepository, UserService userService) {
    this.postRepository = postRepository;
    this.userService = userService;
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

  public void savePostToUser(Post post, User user) {
    user = userService.find(user);

    post.setUser(user);
    post.setCreationDateTime(ZonedDateTime.now());
    postRepository.save(post);

    user.getPosts().add(post);
    userService.save(user);
  }
}
 