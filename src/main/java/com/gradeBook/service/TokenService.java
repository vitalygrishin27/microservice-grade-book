package com.gradeBook.service;

import com.gradeBook.entity.Token;
import com.gradeBook.entity.User;
import com.gradeBook.repository.TokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TokenService {
    static final String TOKE_VALID_TIME = "TOKE_VALID_TIME";
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private final TokenRepo tokenRepo;
    private final AppSettingsService appSettingsService;

    public Token findTokenByString(String code) {
        return tokenRepo.findByToken(code);
    }

    public Token create(User user) {
        if(user.getToken()!=null){
           // Token token = user.getToken();
          //  user.setToken(null);
            tokenRepo.delete(user.getToken());
            user.setToken(null);
        }
        Token token = new Token();
        token.setToken(generateNewToken());
        token.setUser(user);
        token.setAccessLevel(user.getAccessLevel().getLevel().toString());
        token.setFirstName(user.getFirstName());
        token.setValidTo(LocalDateTime.now().plusMinutes(Long.parseLong(appSettingsService.findByName(TOKE_VALID_TIME).getValue())));
        save(token);
        return token;
    }

    public Token save(Token token) {
        return tokenRepo.saveAndFlush(token);
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
