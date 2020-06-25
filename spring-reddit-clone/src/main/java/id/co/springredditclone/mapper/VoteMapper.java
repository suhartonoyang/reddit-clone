package id.co.springredditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import id.co.springredditclone.dto.VoteDto;
import id.co.springredditclone.model.Post;
import id.co.springredditclone.model.User;
import id.co.springredditclone.model.Vote;

@Mapper(componentModel = "spring")
public interface VoteMapper {

	@Mapping(target = "post", source = "post")
	@Mapping(target = "user", source = "user")
	Vote mapToVote(VoteDto voteDto, Post post, User user);
}
