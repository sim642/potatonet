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
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import ee.potatonet.eid.EIDDetails;

@Entity
public class User implements UserDetails {

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
  
  @Transient
  private Set<? extends GrantedAuthority> authorities;
  
  public User(String idCode, String estMail, String name) {
    this.idCode = idCode;
    this.estMail = estMail;
    this.name = name;
    this.posts = new ArrayList<>();
    this.friends = new HashSet<>();
    this.incomingFriendRequests = new HashSet<>();
  }

  public User() {
  }

  public User(EIDDetails eidDetails) {
    this(eidDetails.getIdCode(), eidDetails.getEmail(), eidDetails.getFullName());
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

  //Copy-paste from spring security User
  private static SortedSet<GrantedAuthority> sortAuthorities(
      Collection<? extends GrantedAuthority> authorities) {
    Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
    // Ensure array iteration order is predictable (as per
    // UserDetails.getAuthorities() contract and SEC-717)
    SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<GrantedAuthority>(
        new AuthorityComparator());

    for (GrantedAuthority grantedAuthority : authorities) {
      Assert.notNull(grantedAuthority,
          "GrantedAuthority list cannot contain any null elements");
      sortedAuthorities.add(grantedAuthority);
    }

    return sortedAuthorities;
  }

  public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
    this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
  }

  private static class AuthorityComparator implements Comparator<GrantedAuthority>,
      Serializable {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public int compare(GrantedAuthority g1, GrantedAuthority g2) {
      // Neither should ever be null as each entry is checked before adding it to
      // the set.
      // If the authority is null, it is a custom authority and should precede
      // others.
      if (g2.getAuthority() == null) {
        return -1;
      }

      if (g1.getAuthority() == null) {
        return 1;
      }

      return g1.getAuthority().compareTo(g2.getAuthority());
    }
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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return estMail;
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
