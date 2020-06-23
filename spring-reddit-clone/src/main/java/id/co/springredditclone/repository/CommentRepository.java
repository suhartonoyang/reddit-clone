package id.co.springredditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import id.co.springredditclone.model.Comment;
import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPost(Post post);

	List<Comment> findAllByUser(User user);
}
