package ee.potatonet.data.repos;

import java.time.ZonedDateTime;
import java.util.List;

import ee.potatonet.data.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ee.potatonet.data.Post;
import ee.potatonet.data.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query(value = "SELECT * FROM V_FEED WHERE FEED_USER_ID=?1 AND CREATION_DATE_TIME<?2 LIMIT ?3", nativeQuery = true)
  List<Post> findAllFeedPosts(User user, ZonedDateTime beforeDateTime, int count);

  @Query(value = "SELECT * FROM V_USER_FEED WHERE FEED_USER_ID=?1 AND CREATION_DATE_TIME<?2 LIMIT ?3", nativeQuery = true)
  List<Post> findAllUserFeedPosts(User user, ZonedDateTime beforeDateTime, int count);

  @Query(value = "select post.coordinates from Post post")
  List<Coordinates> findAllPostCoordinates();

  @Modifying
  @Query(value = "INSERT INTO POST_LIKE (user_id, post_id) VALUES (?1, ?2)", nativeQuery = true)
  void saveLike(User user, Post post);

  @Modifying
  @Query(value = "DELETE FROM POST_LIKE WHERE user_id=?1 AND post_id = ?2", nativeQuery = true)
  void removeLike(User user, Post post);
}
