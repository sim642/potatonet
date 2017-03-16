package ee.potatonet.data;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ee.potatonet.auth.eid.EIDDetails;

@Entity
public class User extends TransientAuthoritiesUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String password;
  private String googleId;

  @Enumerated(EnumType.STRING)
  private Language language = Language.EN;

  @Embedded
  private EIDDetails eid;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Post> posts;
  @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
  @JoinTable(name = "tbl_friends",
      joinColumns = @JoinColumn(name = "personId"),
      inverseJoinColumns = @JoinColumn(name = "friendId")
  )
  private Set<User> friends;
  @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
  @JoinTable(name = "tbl_friend_requests",
      joinColumns = @JoinColumn(name = "personId"),
      inverseJoinColumns = @JoinColumn(name = "friendId")
  )
  private Set<User> incomingFriendRequests;

  @ManyToMany(mappedBy = "incomingFriendRequests")
  private Set<User> outgoingFriendRequests;

  public User() {

  }

  public User(EIDDetails eid) {
    this.eid = eid;
    this.posts = new ArrayList<>();
    this.friends = new HashSet<>();
    this.incomingFriendRequests = new HashSet<>();
  }

  public Long getId() {
    return id;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public EIDDetails getEid() {
    return eid;
  }

  public String getFullName() {
    return eid.getFullName();
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

  public Set<User> getOutgoingFriendRequests() {
    return outgoingFriendRequests;
  }

  public Set<User> getFriends() {
    return friends;
  }

  public void setFriends(Set<User> friends) {
    this.friends = friends;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public String getGoogleId() {
    return googleId;
  }

  public void setGoogleId(String googleId) {
    this.googleId = googleId;
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
        ", eid=" + eid +
        '}';
  }

  @Override
  public String getUsername() {
    return eid.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
