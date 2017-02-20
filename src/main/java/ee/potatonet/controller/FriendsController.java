package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.potatonet.data.User;
import ee.potatonet.data.repos.UserRepository;

@Controller
public class FriendsController {
  
  private final UserRepository userRepository;

  @Autowired
  public FriendsController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping(value = "/friends")
  public String doGet(Model model) {
    // TODO: Add logic
    return "friends";
  }
  
  @PostMapping(value = "/friends/accept/{id}")
  public String acceptFriendRequest(@PathVariable("id") long id, @CurrentUser User currentUser) {
    User newFriend = userRepository.findOne(id);
    currentUser.addFriend(newFriend);
    currentUser.getIncomingFriendRequests().remove(newFriend);
    
    return "redirect:/friends";
  }

  @PostMapping(value = "/friends/reject/{id}")
  public String rejectFriendRequest(@PathVariable("id") long id, @CurrentUser User currentUser) {
    currentUser.getIncomingFriendRequests().remove(userRepository.findOne(id));

    return "redirect:/friends";
  }

}
