package ee.potatonet.data.repos;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
  
  List<Post> findAllPostsByUserOrderByCreationDateTimeDesc(User user);
  
  default List<Post> findAllPostsByFriendsAndMe(User user) {
    return Stream.concat(findAllPostsFromFriends(user).stream(), findAllPostsByUserOrderByCreationDateTimeDesc(user).stream())
        .sorted(Comparator.comparing(Post::getCreationDateTime).reversed())
        .collect(Collectors.toList());
  }
}
