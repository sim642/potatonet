package ee.potatonet.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(columnDefinition= "LONGVARCHAR")
  @NotNull
  @Size(min = 1, max = 32700)
  private String content;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private ZonedDateTime creationDateTime;

  public Comment(Post post, User user, String content) {
    this.post = post;
    this.user = user;
    this.content = content;
  }

  public Comment() {
  }

  public Long getId() {
    return id;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content.trim();
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

    Comment comment = (Comment) o;

    return id != null ? id.equals(comment.id) : comment.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Comment{" +
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
