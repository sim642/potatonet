package ee.potatonet.data.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ee.potatonet.data.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findOneByEstMail(String estMail);
}
