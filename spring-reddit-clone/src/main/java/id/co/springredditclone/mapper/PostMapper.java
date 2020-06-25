package id.co.springredditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import id.co.springredditclone.dto.PostRequest;
import id.co.springredditclone.dto.PostResponse;
import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.Subreddit;
import id.co.springredditclone.model.User;

@Mapper(componentModel = "spring")
public interface PostMapper {

	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "description", source = "postRequest.description")
	@Mapping(target = "subreddit", source = "subreddit")
	@Mapping(target = "user", source = "user")
	Post mapToPost(PostRequest postRequest, Subreddit subreddit, User user);

	@Mapping(target = "id", source = "postId")
	@Mapping(target = "subredditName", source = "subreddit.name")
	@Mapping(target = "username", source = "user.username")
	PostResponse mapToPostResponse(Post post);
}
