package id.co.springredditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.springredditclone.dto.CommentDto;
import id.co.springredditclone.exceptions.PostNotFoundException;
import id.co.springredditclone.mapper.CommentMapper;
import id.co.springredditclone.model.Comment;
import id.co.springredditclone.model.NotificationEmail;
import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.User;
import id.co.springredditclone.repository.CommentRepository;
import id.co.springredditclone.repository.PostRepository;
import id.co.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final AuthService authService;
	private final CommentMapper commentMapper;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	private static final String POST_URL = "";

	public void save(CommentDto commentDto) {

		Post post = postRepository.findById(commentDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException(
						"Post ID: " + commentDto.getPostId().toString() + " not found"));

		Comment comment = commentMapper.mapToComment(commentDto, post,
				authService.getCurrentUser());

		commentRepository.save(comment);

		if (commentRepository.existsById(comment.getId())) {
			String message = post.getUser().getUsername() + " posted a comment on your post."
					+ POST_URL;
			sendCommentNotification(message, post.getUser());
		}

	}

	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post",
				user.getEmail(), message));
	}

	public List<CommentDto> getAllCommentsForPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new PostNotFoundException("Post ID: " + postId.toString() + " not found"));

		return commentRepository.findAllByPost(post).stream().map(commentMapper::mapToCommentDto)
				.collect(Collectors.toList());
	}

	public List<CommentDto> getAllCommentsForUser(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("Username : " + username + " not found"));

		return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToCommentDto)
				.collect(Collectors.toList());

	}
}
