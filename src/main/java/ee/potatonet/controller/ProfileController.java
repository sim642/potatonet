package ee.potatonet.controller;

import ee.potatonet.data.User;
import ee.potatonet.data.repos.PostRepository;
import ee.potatonet.data.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {

  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Autowired
  public ProfileController(UserRepository userRepository, PostRepository postRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
  public String doGet(@PathVariable("id") long id, Model model) {
    User user = userRepository.findOne(id);
    model.addAttribute("user", user);
    model.addAttribute("posts", postRepository.findAllPostsByUserOrderByCreationDateTimeDesc(user));

    return "profile";
  }

  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public String doGet(@CurrentUser User currentUser, Model model) {
    model.addAttribute("user", currentUser);
    model.addAttribute("posts", postRepository.findAllPostsByUserOrderByCreationDateTimeDesc(currentUser));

    return "profile";
  }

  @PostMapping(value = "/profile/add/{id}")
  public String doGet(@CurrentUser User currentUser, @PathVariable("id") long id, Model model) {
    currentUser = userRepository.findOne(currentUser.getId());
    User user = userRepository.findOne(id);
    user.getIncomingFriendRequests().add(currentUser);
    userRepository.save(user);
    userRepository.save(currentUser);

    return "redirect:/profile/" + id;
  }
}
