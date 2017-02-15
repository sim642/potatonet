package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.potatonet.data.User;
import ee.potatonet.data.repos.PostRepository;
import ee.potatonet.data.repos.UserRepository;

@Controller
public class ProfileController {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Autowired
  public ProfileController(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
  public String doGet(@PathVariable("id") long id, Model model) {
    User one = userRepository.findOne(id);
    System.out.println(one);
    model.addAttribute("posts", postRepository.findAllPostsByUserOrderByCreationDateTimeDesc(one));

    return "profile";
  }

  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public String doGet(@CurrentUser User currentUser, Model model) {
    model.addAttribute("posts", postRepository.findAllPostsByUserOrderByCreationDateTimeDesc(currentUser));
    // TODO: Add logic
    return "profile";
  }
}
