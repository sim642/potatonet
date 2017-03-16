package ee.potatonet.configuration;

import javax.annotation.PostConstruct;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ee.potatonet.auth.eid.EIDCodeDetails;
import ee.potatonet.auth.eid.EIDDetails;
import ee.potatonet.data.model.Post;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.PostService;
import ee.potatonet.data.service.UserService;

@Configuration
@Profile("dev")
public class DevConfiguration {

  @Autowired
  private PostService postService;
  @Autowired
  private UserService userService;

  private final Random random = new Random(1337L);

  @PostConstruct
  public void mockSetUp() {
    User veiko = createUser("38001230000", "Veiko", "Kääp", "veiko.kaap@eesti.ee", "veiko");
    User tiit = createUser("37001230000", "Tiit", "Oja", "tiit.oja@eesti.ee", "tiit");
    User simmo = createUser("36001230000", "Simmo", "Saan", "simmo.saan@eesti.ee", "simmo");

    User wannaBeFriend = createUser("49510201111", "Wannabe", "Friend", "wannabe@eesti.ee", "wannabe");
    veiko.getIncomingFriendRequests().add(wannaBeFriend);
    simmo.getIncomingFriendRequests().add(wannaBeFriend);
    tiit.getIncomingFriendRequests().add(wannaBeFriend);

    createPost(veiko, "Tere, ma Veiko");
    createPost(tiit, "Tere, ma Tiit");
    createPost(simmo, "Tere, ma Simmo");

    for (int i = 0; i < 250; i++) {
      createPost(simmo, String.format("Post %d", i));
    }

    veiko.addFriend(tiit);
    veiko.addFriend(simmo);
    tiit.addFriend(simmo);

    userService.save(veiko);
    userService.save(tiit);
    userService.save(simmo);
  }

  private User createUser(String idCode, String givenName, String surname, String email, String password) {
    User user = new User(new EIDDetails(new EIDCodeDetails(idCode), givenName, surname, email));
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPassword(passwordEncoder.encode(password));
    return userService.save(user);
  }

  private void createPost(User user, String content) {
    Post post = new Post();
    // Test post coordinates near Estonia
    post.setLatitude(random.nextFloat() +  58.0f);
    post.setLongitude(random.nextFloat() +  26.0f);
    post.setContent(content);
    postService.savePostToUser(post, user);
  }
}
