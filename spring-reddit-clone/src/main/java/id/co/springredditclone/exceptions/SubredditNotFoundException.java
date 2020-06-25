package id.co.springredditclone.exceptions;

public class SubredditNotFoundException extends RuntimeException{

	public SubredditNotFoundException (String message) {
		super(message);
	}
}
