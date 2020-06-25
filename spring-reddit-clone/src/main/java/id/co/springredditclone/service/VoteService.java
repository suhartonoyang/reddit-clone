package id.co.springredditclone.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.springredditclone.dto.VoteDto;
import id.co.springredditclone.exceptions.PostNotFoundException;
import id.co.springredditclone.exceptions.SpringRedditException;
import id.co.springredditclone.mapper.VoteMapper;
import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.Vote;
import id.co.springredditclone.model.VoteType;
import id.co.springredditclone.repository.PostRepository;
import id.co.springredditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

	private final VoteRepository voteRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	private final VoteMapper voteMapper;

	public void vote(VoteDto voteDto) {
		Post post = postRepository.findById(voteDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException(
						"Post ID: " + voteDto.getPostId().toString() + " not found"));

		Optional<Vote> voteByPostAndUser = voteRepository
				.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

		if (voteByPostAndUser.isPresent()
				&& voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
			throw new SpringRedditException(
					"You have already " + voteDto.getVoteType() + "D for this post");
		}

		if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
			post.setVoteCount(post.getVoteCount() + 1);
		} else {
			post.setVoteCount(post.getVoteCount() - 1);
		}

		Vote vote = voteMapper.mapToVote(voteDto, post, authService.getCurrentUser());

		voteRepository.save(vote);
		postRepository.save(post);

	}

}
