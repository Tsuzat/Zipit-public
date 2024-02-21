package org.example.backend.controller.auth;

import lombok.RequiredArgsConstructor;
import org.example.backend.bean.UserBean;
import org.example.backend.bean.auth.AuthRequestBean;
import org.example.backend.bean.auth.AuthResponseBean;
import org.example.backend.bean.auth.RegisterRequestBean;
import org.example.backend.dao.LilyUserDAO;
import org.example.backend.servies.auth.LilyAuthService;
import org.example.backend.servies.email.LilyEmailService;
import org.example.backend.utilies.LilyBasicUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LilyAuthController {

    private final LilyAuthService lilyAuthService;
    private final LilyUserDAO lilyUserDAO;
    private final LilyEmailService lilyEmailService;
    private final PasswordEncoder passwordEncoder;

    /// TODO: add this
    private final String baseFrontEndURL = "http://localhost:5173";

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequestBean request
    ) {
        try {
            return ResponseEntity.ok(lilyAuthService.register(request));
        } catch (Exception e) {
            // TODO: Better logging for Errors
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseBean> authenticate(
            @RequestBody AuthRequestBean request
    ) {
        return ResponseEntity.ok(lilyAuthService.authenticate(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String token,
            @RequestParam String email
    ) {

        Optional<UserBean> userBeanOptional = lilyUserDAO.findUserBeanByEmailAndToken(email, token);
        if (userBeanOptional.isEmpty()) {
            // TODO: Return better information on unverified situations
            return ResponseEntity.ok("Token or(and) email is incorrect");
        }
        UserBean userBean = userBeanOptional.get();

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create(baseFrontEndURL));

        // If User is already verified then return it
        if (userBean.isEnabled()) {
            return new ResponseEntity<>(headers, HttpStatus.PERMANENT_REDIRECT);
        }

        boolean isTokenExpired = userBean
                .getTokenExpiresOn()
                .before(new Date(System.currentTimeMillis()));

        // If Token is expired then ask user to signup again
        if (isTokenExpired)
            return ResponseEntity.ok("Token expired. Please sign up again");


        // Enable the user
        userBean.setVerified(true);
        lilyUserDAO.save(userBean);

        return new ResponseEntity<>(headers, HttpStatus.PERMANENT_REDIRECT);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePasswordRequest(
            @RequestBody Map<String, String> payload
    ) {
        String email = payload.get("email");
        System.out.println("Email = " + email);
        // Generate a new token
        String token = LilyBasicUtils.generateRandomHash(20);
        // Get the user by UseName
        Optional<UserBean> userBeanOptional = lilyUserDAO.findUserBeanByEmail(email);
        if (userBeanOptional.isEmpty()) {
            return new ResponseEntity<>("User is not found", HttpStatus.BAD_REQUEST);
        }
        UserBean userBean = userBeanOptional.get();
        userBean.setToken(token);
        userBean.setTokenExpiresOn(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
        userBean = lilyUserDAO.save(userBean);

        String baseUrl = baseFrontEndURL + "/change-password";
        String url = "%s?token=%s&email=%s"
                .formatted(baseUrl, token, email);
        // HTML content for the email
        String emailString = """
                <h2> Change Your Password <h2>
                <p> <a href="%s" target="_blank"> Click here </a> to change your password</p>
                <h5> Token will expire in 15 minutes</h5>
                	""".formatted(url);
        // Send the email
        lilyEmailService.sendEmail(userBean.getEmail(), "Password Change", emailString);
        return ResponseEntity.ok("Check email for password change");
    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String email = payload.get("email");
        String password = payload.get("password");
        Optional<UserBean> urlBeanOptional = lilyUserDAO.findUserBeanByEmailAndToken(email, token);
        if (urlBeanOptional.isEmpty())
            return new ResponseEntity<>("Can't found user", HttpStatus.INTERNAL_SERVER_ERROR);

        UserBean userBean = urlBeanOptional.get();

        System.out.println("Old User : " + userBean);

        // Check if the token is expired
        boolean isTokenExpired = userBean
                .getTokenExpiresOn()
                .before(new Date(System.currentTimeMillis()));
        if (isTokenExpired)
            return new ResponseEntity<>("Token Expired", HttpStatus.BAD_REQUEST);

        // Set the new password
        userBean.setPassword(passwordEncoder.encode(password));

        userBean = lilyUserDAO.save(userBean);
        System.out.println("New User : " + userBean);

        return ResponseEntity.ok("Password Changed");
    }
}
