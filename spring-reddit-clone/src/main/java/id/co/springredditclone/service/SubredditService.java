package id.co.springredditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.springredditclone.dto.SubredditDto;
import id.co.springredditclone.model.Subreddit;
import id.co.springredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

	private final SubredditRepository subredditRepository;

	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = subredditRepository.save(mapSubredditDto(subredditDto));
		subredditDto.setId(subreddit.getId());
		return subredditDto;
	}

	private Subreddit mapSubredditDto(SubredditDto subredditDto) {
		return Subreddit.builder().name(subredditDto.getName())
				.description(subredditDto.getDescription()).build();

	}

	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll().stream().map(this::mapToDto)
				.collect(Collectors.toList());
	}

	private SubredditDto mapToDto(Subreddit subreddit) {
		return SubredditDto.builder().id(subreddit.getId()).name(subreddit.getName())
				.numberOfPosts(subreddit.getPosts().size()).build();
	}
}
