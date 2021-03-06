package ee.potatonet.data.repository;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ee.potatonet.data.model.Coordinates;
import ee.potatonet.data.model.Post;
import ee.potatonet.data.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query(value = "SELECT * FROM V_FEED WHERE FEED_USER_ID=?1 AND CREATION_DATE_TIME<?2 LIMIT ?3", nativeQuery = true)
  List<Post> findAllFeedPosts(User user, ZonedDateTime beforeDateTime, int count);

  @Query(value = "SELECT * FROM V_USER_FEED WHERE FEED_USER_ID=?1 AND CREATION_DATE_TIME<?2 LIMIT ?3", nativeQuery = true)
  List<Post> findAllUserFeedPosts(User user, ZonedDateTime beforeDateTime, int count);

  @Query(value = "select post.coordinates from Post post where post.coordinates.longitude is not null and post.coordinates.latitude is not null")
  List<Coordinates> findAllPostCoordinates();

  @Modifying
  @Query(value = "CALL TOGGLE_LIKE(?1, ?2)", nativeQuery = true)
  void toggleLike(Long postId, Long userId);
}
