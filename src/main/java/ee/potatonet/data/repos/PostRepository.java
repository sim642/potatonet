package ee.potatonet.data.repos;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ee.potatonet.data.Post;
import ee.potatonet.data.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query(value = "SELECT * FROM V_FEED WHERE FEED_USER_ID=?1 AND CREATION_DATE_TIME<?2 LIMIT ?3", nativeQuery = true)
  List<Post> findAllFeedPosts(User user, ZonedDateTime beforeDateTime, int count);
}
