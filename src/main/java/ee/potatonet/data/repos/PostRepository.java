package ee.potatonet.data.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ee.potatonet.data.Post;
import ee.potatonet.data.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  @Query(
      "select post from Post post " +
          "join post.user.friends friend where friend=:user " +
          "order by post.creationDateTime desc "
  )
  List<Post> findAllPostsFromFriends(@Param(value = "user") User user);
}
