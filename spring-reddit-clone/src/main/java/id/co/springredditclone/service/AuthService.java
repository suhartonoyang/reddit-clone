package id.co.springredditclone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.springredditclone.dto.AuthenticationResponse;
import id.co.springredditclone.dto.LoginRequest;
import id.co.springredditclone.dto.RefreshTokenRequest;
import id.co.springredditclone.dto.RegisterRequest;
import id.co.springredditclone.exceptions.SpringRedditException;
import id.co.springredditclone.model.NotificationEmail;
import id.co.springredditclone.model.User;
import id.co.springredditclone.model.VerificationToken;
import id.co.springredditclone.repository.UserRepository;
import id.co.springredditclone.repository.VerificationTokenRepository;
import id.co.springredditclone.security.JwtProvider;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;

	private final Environment environment;

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
						+ environment.getProperty("account.verification.url") + token));

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

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		return AuthenticationResponse.builder().authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(loginRequest.getUsername()).build();
	}

	@Transactional(readOnly = true)
	public User getCurrentUser() {
		UserDetails principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		return userRepository.findByUsername(principal.getUsername())
				.orElseThrow(() -> new SpringRedditException("Username not found - " + principal.getUsername()));
	}

	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());

		return AuthenticationResponse.builder().authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(refreshTokenRequest.getUsername()).build();

	}
}
