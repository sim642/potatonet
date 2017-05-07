package ee.potatonet.data.service;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.potatonet.data.model.Comment;
import ee.potatonet.data.model.Coordinates;
import ee.potatonet.data.model.Post;
import ee.potatonet.data.model.User;
import ee.potatonet.data.repository.CommentRepository;
import ee.potatonet.data.repository.PostRepository;

@Service
@Transactional
public class PostService {

  private static final int FEED_COUNT = 15;

  private final PostRepository postRepository;
  private final UserService userService;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final CommentRepository commentRepository;

  @Autowired
  public PostService(PostRepository postRepository, UserService userService, SimpMessagingTemplate simpMessagingTemplate, CommentRepository commentRepository) {
    this.postRepository = postRepository;
    this.userService = userService;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.commentRepository = commentRepository;
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

  public Post save(Post post) {
    return postRepository.save(post);
  }

  public Post savePostToUser(Post post, User user) {
    user = userService.find(user);

    post.setUser(user);
    post.setCreationDateTime(ZonedDateTime.now());
    post = postRepository.save(post);

    user.getPosts().add(post);
    user = userService.save(user);

    simpMessagingTemplate.convertAndSend("/topic/posts/" + user.getId(), post.getId());

    return post;
  }

  public Comment saveCommentToPost(Comment comment, Post post) {
    post = find(post);

    comment.setPost(post);
    comment.setCreationDateTime(ZonedDateTime.now());
    comment = commentRepository.save(comment);

    post.getComments().add(comment);
    post = save(post);

    simpMessagingTemplate.convertAndSend("/topic/comments/" + post.getId(), comment.getId());

    return comment;
  }

  public List<Coordinates> getAllPostCoordinates() {
    return postRepository.findAllPostCoordinates();
  }

  public void toggleLike(User user, Long postId) {
    postRepository.toggleLike(postId, user.getId());

    simpMessagingTemplate.convertAndSend("/topic/likes/" + postId, findById(postId).getLikeCount());
  }
}