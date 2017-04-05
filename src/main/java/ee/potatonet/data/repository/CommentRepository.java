package ee.potatonet.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ee.potatonet.data.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
