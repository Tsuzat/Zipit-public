package org.example.backend.servies.auth;

import lombok.RequiredArgsConstructor;
import org.example.backend.bean.Role;
import org.example.backend.bean.UserBean;
import org.example.backend.bean.auth.AuthRequestBean;
import org.example.backend.bean.auth.AuthResponseBean;
import org.example.backend.bean.auth.RegisterRequestBean;
import org.example.backend.config.JwtService;
import org.example.backend.dao.LilyUserDAO;
import org.example.backend.servies.email.LilyEmailService;
import org.example.backend.utilies.LilyBasicUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LilyAuthService {

    private final LilyUserDAO lilyUserDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final LilyEmailService lilyEmailService;

    // TODO: Change it accordingly
    private final String backendURL = "http://localhost:8090";

    public String register(RegisterRequestBean requestBean) throws Exception {
        // If the requestBean has any empty value, throw exception
        if (
                requestBean.getEmail().isEmpty() || requestBean.getEmail().isBlank() ||
                        requestBean.getPassword().isEmpty() || requestBean.getPassword().isBlank() ||
                        requestBean.getFirstName().isEmpty() || requestBean.getFirstName().isBlank() ||
                        requestBean.getLastName().isEmpty() || requestBean.getLastName().isBlank()
        ) {
            throw new Exception("User Details are either empty or blank");
        }

        UserBean userBean = UserBean
                .builder()
                .email(requestBean.getEmail())
                .firstName(requestBean.getFirstName())
                .lastName(requestBean.getLastName())
                .password(passwordEncoder.encode(requestBean.getPassword()))
                .createdOn(new Date())
                .role(Role.USER)
                .maxUrls(5)
                .token(LilyBasicUtils.generateRandomHash(20))
                .tokenExpiresOn(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // Expired on 15 min
                .build();
        lilyUserDAO.save(userBean);

        // TODO: Put Base URL as an Environment Variable
        String url = "%s/api/auth/verify-email?token=%s&email=%s"
                .formatted(backendURL, userBean.getToken(), userBean.getEmail());
        // HTML content for the email
        String email = """
                <h2> Email Verification <h2>
                <p> <a href="%s" target="_blank"> Click here </a> to verify your email</p>
                <h5> Token will expire in 15 minutes</h5>
                	""".formatted(url);
        // Send the email
        lilyEmailService.sendEmail(userBean.getEmail(), "Confirm You Email", email);
        return "Please check you email for verification";
    }

    public AuthResponseBean authenticate(AuthRequestBean request) {
        System.out.println("Authenticate Runs");
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserBean userBean = lilyUserDAO
                .findUserBeanByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(userBean);
        return AuthResponseBean
                .builder()
                .token(jwtToken)
                .build();
    }
}
