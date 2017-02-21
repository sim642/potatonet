package ee.potatonet.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;

@Entity
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  private String content;
  
  private ZonedDateTime creationDateTime;

  public Post(User user, String content) {
    this.user = user;
    this.content = content;
  }

  public Post() {
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Post post = (Post) o;

    return id != null ? id.equals(post.id) : post.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Post{" +
        "id=" + id +
        ", user=" + user +
        ", content='" + content + '\'' +
        '}';
  }

  public ZonedDateTime getCreationDateTime() {
    return creationDateTime;
  }

  public void setCreationDateTime(ZonedDateTime creationDateTime) {
    this.creationDateTime = creationDateTime;
  }
}
