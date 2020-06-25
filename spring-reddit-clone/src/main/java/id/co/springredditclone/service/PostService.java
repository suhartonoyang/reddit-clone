package id.co.springredditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.springredditclone.dto.PostRequest;
import id.co.springredditclone.dto.PostResponse;
import id.co.springredditclone.exceptions.PostNotFoundException;
import id.co.springredditclone.exceptions.SubredditNotFoundException;
import id.co.springredditclone.mapper.PostMapper;
import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.Subreddit;
import id.co.springredditclone.model.User;
import id.co.springredditclone.repository.PostRepository;
import id.co.springredditclone.repository.SubredditRepository;
import id.co.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

	private final PostRepository postRepository;
	private final SubredditRepository subredditRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final PostMapper postMapper;

	public void save(PostRequest postRequest) {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
				.orElseThrow(() -> new SubredditNotFoundException(
						"Subreddit ID: " + postRequest.getSubredditName() + " not found"));

		postRepository
				.save(postMapper.mapToPost(postRequest, subreddit, authService.getCurrentUser()));
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		return postRepository.findAll().stream().map(postMapper::mapToPostResponse)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new PostNotFoundException("Post ID: " + postId.toString() + " not found"));

		return postMapper.mapToPostResponse(post);
	}

	public List<PostResponse> getPostsBySubreddit(Long subredditId) {
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(() -> new SubredditNotFoundException(
						"Subreddit ID: " + subredditId.toString() + " not found"));

		List<Post> posts = postRepository.findAllBySubreddit(subreddit);

		return posts.stream().map(postMapper::mapToPostResponse).collect(Collectors.toList());
	}

	public List<PostResponse> getPostsByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("Username : " + username + " not found"));

		return postRepository.findByUser(user).stream().map(postMapper::mapToPostResponse)
				.collect(Collectors.toList());
	}

}
