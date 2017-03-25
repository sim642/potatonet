package ee.potatonet.data.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(columnDefinition= "LONGVARCHAR")
  @NotNull
  @Size(min = 1, max = 32700)
  private String content;

  private Integer likeCount = 0;

  @Embedded
  private Coordinates coordinates = new Coordinates();

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private ZonedDateTime creationDateTime;

  public Post(User user, String content) {
    this.user = user;
    this.content = content;
  }

  public Post() {
  }

  public Long getId() {
    return id;
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

  public Float getLongitude() {
    return coordinates.getLongitude();
  }

  public void setLongitude(Float longitude) {
    coordinates.setLongitude(longitude);
  }

  public Float getLatitude() {
    return coordinates.getLatitude();
  }

  public void setLatitude(Float latitude) {
    coordinates.setLatitude(latitude);
  }

  public Coordinates getCoordinates() {
    return coordinates;
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

  public Integer getLikeCount() {
    return likeCount == null ? 0 : likeCount;
  }

  public void setLikeCount(int likeCount) {
    this.likeCount = likeCount;
  }
}
