package id.co.springredditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import id.co.springredditclone.dto.CommentDto;
import id.co.springredditclone.model.Comment;
import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	@Mapping(target = "id", ignore = true)
//	@Mapping(target = "text", source = "commentDto.text")
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "post", source = "post")
	@Mapping(target = "user", source = "user")
	Comment mapToComment(CommentDto commentDto, Post post, User user);

	@Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
	@Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
	CommentDto mapToCommentDto(Comment comment);

}
