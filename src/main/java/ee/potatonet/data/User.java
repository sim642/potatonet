package ee.potatonet.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String idCode;
  private String estMail;
  private String name;
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Post> posts;
  @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
  @JoinTable(name = "tbl_friends",
      joinColumns = @JoinColumn(name = "personId"),
      inverseJoinColumns = @JoinColumn(name = "friendId")
  )
  private Set<User> friends;
  @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
  @JoinTable(name = "tbl_friend_requests",
      joinColumns = @JoinColumn(name = "personId"),
      inverseJoinColumns = @JoinColumn(name = "friendId")
  )
  private Set<User> incomingFriendRequests;

  public User(String idCode, String estMail, String name, List<Post> posts, Set<User> friends, Set<User> incomingFriendRequests) {
    this.idCode = idCode;
    this.estMail = estMail;
    this.name = name;
    this.posts = posts;
    this.friends = friends;
    this.incomingFriendRequests = incomingFriendRequests;
  }

  public User() {
  }

  public String getIdCode() {
    return idCode;
  }

  public void setIdCode(String idCode) {
    this.idCode = idCode;
  }

  public String getEstMail() {
    return estMail;
  }

  public void setEstMail(String estMail) {
    this.estMail = estMail;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public void addFriend(User newFriend) {
    this.getFriends().add(newFriend);
    newFriend.getFriends().add(this);
  }

  public void removeFriend(User friendNoMore) {
    this.getFriends().remove(friendNoMore);
    friendNoMore.getFriends().remove(this);
  }

  public Set<User> getIncomingFriendRequests() {
    return incomingFriendRequests;
  }

  public void setIncomingFriendRequests(Set<User> incomingFriendRequests) {
    this.incomingFriendRequests = incomingFriendRequests;
  }

  public Set<User> getFriends() {
    return friends;
  }

  public void setFriends(Set<User> friends) {
    this.friends = friends;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", idCode='" + idCode + '\'' +
        ", estMail='" + estMail + '\'' +
        ", name='" + name + '\'' +
        ", posts=" + posts.stream() +
        ", friends={" + friends.stream().map(User::getName).collect(Collectors.joining(",")) + "}" +
        ", incomingFriendRequests={" + incomingFriendRequests.stream().map(User::getName).collect(Collectors.joining(",")) + "}" +
        '}';
  }
}
