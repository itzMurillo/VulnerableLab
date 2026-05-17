package br.unipar.frameworks.security;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

final class MessageDigestSupport {

    private MessageDigestSupport() {
    }

    static boolean constantTimeEquals(String expected, String actual) {
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                actual.getBytes(StandardCharsets.UTF_8)
        );
    }
}
