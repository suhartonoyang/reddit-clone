package id.co.springredditclone.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import id.co.springredditclone.dto.PostRequest;
import id.co.springredditclone.dto.PostResponse;
import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.Subreddit;
import id.co.springredditclone.model.User;
import id.co.springredditclone.model.Vote;
import id.co.springredditclone.model.VoteType;
import id.co.springredditclone.repository.CommentRepository;
import id.co.springredditclone.repository.VoteRepository;
import id.co.springredditclone.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private AuthService authService;

	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "description", source = "postRequest.description")
	@Mapping(target = "subreddit", source = "subreddit")
	@Mapping(target = "user", source = "user")
	@Mapping(target = "voteCount", constant = "0")
	public abstract Post mapToPost(PostRequest postRequest, Subreddit subreddit, User user);

	@Mapping(target = "id", source = "postId")
	@Mapping(target = "subredditName", source = "subreddit.name")
	@Mapping(target = "username", source = "user.username")
	@Mapping(target = "commentCount", expression = "java(commentCount(post))")
	@Mapping(target = "duration", expression = "java(getDuration(post))")
	@Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
	@Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
	public abstract PostResponse mapToPostResponse(Post post);

	Integer commentCount(Post post) {
		return commentRepository.findAllByPost(post).size();
	}

	String getDuration(Post post) {
		return TimeAgo.using(post.getCreatedDate().toEpochMilli());
	}

	boolean isPostUpVoted(Post post) {
		return checkVoteType(post, VoteType.UPVOTE);
	}

	boolean isPostDownVoted(Post post) {
		return checkVoteType(post, VoteType.DOWNVOTE);
	}

	private boolean checkVoteType(Post post, VoteType voteType) {
		if (authService.isLoggedIn()) {
			Optional<Vote> voteForPostByUser = voteRepository
					.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
			return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
					.isPresent();
		}
		return false;
	}

}
