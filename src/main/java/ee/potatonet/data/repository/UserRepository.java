package ee.potatonet.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ee.potatonet.data.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findOneByEidEmail(String eidEmail);
  User findOneByGoogleId(String googleId);

  @Query(
      "select count(*) from User user " +
          "join user.incomingFriendRequests friendRequest " +
          "where user=:user"
  )
  Long countIncomingFriendRequests(@Param("user") User user);
}
