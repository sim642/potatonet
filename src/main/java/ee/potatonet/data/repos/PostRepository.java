package ee.potatonet.data.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ee.potatonet.data.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {}
