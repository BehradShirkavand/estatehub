package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.Users;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.scanner.ScannerImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final UsersService usersService;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    // token -> TokenData(email + expiry)
    private final Map<String, TokenData> tokenStore = new HashMap<>();

    // email -> lastRequestTime
    private final Map<String, LocalDateTime> lastRequestTime = new HashMap<>();

    @AllArgsConstructor
    private static class TokenData {
        String email;
        LocalDateTime expiry;
    }

    public boolean sendResetPasswordEmail(String email) {

        if (lastRequestTime.containsKey(email)) {
            LocalDateTime last = lastRequestTime.get(email);
            if (LocalDateTime.now().isBefore(last.plusMinutes(15))) {
                return false;
            }
        }

        boolean userExists = usersService.existsByEmail(email);

        if (!userExists) return false;

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        tokenStore.put(token, new TokenData(email, expiry));

        String resetLink = "http://localhost:8080/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Estatehub: Reset Password");
        message.setText("Please open the reset link if you request for Reset password:\n" + resetLink);

        javaMailSender.send(message);
        return true;
    }

    public boolean isValidResetToken(String token) {
        return tokenStore.containsKey(token);
    }

    public boolean resetPassword(String token, String password) {

        if (!tokenStore.containsKey(token)) return false;

        TokenData tokenData = tokenStore.get(token);

        Users user = usersService.findByEmail(tokenData.email);
        user.setPassword(passwordEncoder.encode(password));
        usersService.save(user);

        tokenStore.remove(token);
        return true;
    }
}
