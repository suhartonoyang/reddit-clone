package id.co.springredditclone.exceptions;

public class PostNotFoundException extends RuntimeException{
	public PostNotFoundException (String message) {
		super(message);
	}
}
