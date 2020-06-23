package id.co.springredditclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.User;
import id.co.springredditclone.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
