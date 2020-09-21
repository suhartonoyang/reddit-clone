package id.co.springredditclone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.co.springredditclone.dto.VoteDto;
import id.co.springredditclone.service.VoteService;
import lombok.AllArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

	private final VoteService voteService;

	@PostMapping
	public ResponseEntity vote(@RequestBody VoteDto voteDto) {
		voteService.vote(voteDto);
		return new ResponseEntity(HttpStatus.CREATED);
	}
}
