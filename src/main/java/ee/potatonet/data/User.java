package ee.potatonet.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class User {
  
  @Id
  @GeneratedValue
  private Long id;
  
  private String idCode;
  private String estMail;
  private String name;
  @Transient
  private List<Post> posts;
  @Transient
  private List<User> friends;
  @Transient
  private List<User> incomingFriendRequests;

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

  public List<User> getFriends() {
    return friends;
  }

  public void setFriends(List<User> friends) {
    this.friends = friends;
  }

  public List<User> getIncomingFriendRequests() {
    return incomingFriendRequests;
  }

  public void setIncomingFriendRequests(List<User> incomingFriendRequests) {
    this.incomingFriendRequests = incomingFriendRequests;
  }
}
