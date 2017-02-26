package ee.potatonet.data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.potatonet.data.repos.UserRepository;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findById(Long id) {
    return userRepository.findOne(id);
  }

  public User find(User user) {
    return findById(user.getId());
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public User findOneByEidEmail(String eidEmail) {
    return userRepository.findOneByEidEmail(eidEmail);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public void addFriendship(User user1, User user2) {
    user1 = find(user1);
    user2 = find(user2);

    user1.getFriends().add(user2);
    user2.getFriends().add(user1);
    user1.getIncomingFriendRequests().remove(user2);
    user2.getIncomingFriendRequests().remove(user1);

    save(user1);
    save(user2);
  }

  public void removeFriendRequests(User user1, User user2) {
    user1 = find(user1);
    user2 = find(user2);

    user1.getIncomingFriendRequests().remove(user2);
    user2.getIncomingFriendRequests().remove(user1);

    save(user1);
    save(user2);
  }

  public void addFriendRequest(User asker, User potentialFriend) {
    asker = find(asker);
    potentialFriend = find(potentialFriend);

    potentialFriend.getIncomingFriendRequests().add(asker);

    save(potentialFriend);
  }

  public Long countUserIncomingFriendRequests(User user) {
    return userRepository.countIncomingFriendRequests(user);
  }

  public List<Long> getUserFeedUserIds(User user) {
    user = find(user);

    return Stream.concat(Stream.of(user), user.getFriends().stream())
        .map(User::getId)
        .collect(Collectors.toList());
  }
}
