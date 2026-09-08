package com.benjamin.Vocabulary.utils;

import java.util.UUID;
import org.springframework.security.oauth2.jwt.Jwt;

public class Utils {
    public static UUID userIdFrom(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
