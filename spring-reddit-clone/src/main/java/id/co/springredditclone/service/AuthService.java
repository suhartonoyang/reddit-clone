package id.co.springredditclone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import id.co.springredditclone.dto.RegisterRequest;
import id.co.springredditclone.exceptions.SpringRedditException;
import id.co.springredditclone.model.NotificationEmail;
import id.co.springredditclone.model.User;
import id.co.springredditclone.model.VerificationToken;
import id.co.springredditclone.repository.UserRepository;
import id.co.springredditclone.repository.VerificationTokenRepository;

@Service
public class AuthService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private MailService mailService;

	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);

		userRepository.save(user);

		String token = generateVerificationToken(user);

		mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(),
				"Thank you for signing up to Spring Reddit, " + "please click on below url to activate your account: "
						+ "http://localhost:8080/api/auth/accountVerification/" + token));

	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();

		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setUser(user);
		verificationToken.setToken(token);

		verificationTokenRepository.save(verificationToken);
		return token;

	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verficationToken = verificationTokenRepository.findByToken(token);
		verficationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verficationToken.get());

	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new SpringRedditException("User not found with name - " + username));

		user.setEnabled(true);
		userRepository.save(user);
	}
}
