package id.co.springredditclone.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.co.springredditclone.dto.PostRequest;
import id.co.springredditclone.dto.PostResponse;
import id.co.springredditclone.service.PostService;
import lombok.AllArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity createPost(@RequestBody PostRequest postRequest) {
		postService.save(postRequest);
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<PostResponse>> getAllPosts() {
		return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
	}

	@GetMapping("/by-subreddit/{subredditId}")
	public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long subredditId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(postService.getPostsBySubreddit(subredditId));
	}

	@GetMapping("/by-username/{username}")
	public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUsername(username));
	}

}
