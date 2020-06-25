package id.co.springredditclone.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.springredditclone.dto.SubredditDto;
import id.co.springredditclone.exceptions.SpringRedditException;
import id.co.springredditclone.mapper.SubredditMapper;
import id.co.springredditclone.model.Subreddit;
import id.co.springredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;

	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = subredditRepository
				.save(subredditMapper.mapDtoToSubreddit(subredditDto));
		subredditDto.setId(subreddit.getId());
		return subredditDto;
	}

//	private Subreddit mapToSubreddit(SubredditDto subredditDto) {
//		return Subreddit.builder().name(subredditDto.getName())
//				.description(subredditDto.getDescription()).createdDate(Instant.now()).build();
//
//	}

	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto)
				.collect(Collectors.toList());
	}

//	private SubredditDto mapToDto(Subreddit subreddit) {
//		return SubredditDto.builder().id(subreddit.getId()).name(subreddit.getName())
//				.numberOfPosts(subreddit.getPosts().size()).build();
//	}

	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));

		return subredditMapper.mapSubredditToDto(subreddit);
	}

}
