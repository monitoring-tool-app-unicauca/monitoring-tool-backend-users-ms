package co.edu.unicauca.monitoring.tool.backend.users.ms.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static co.edu.unicauca.monitoring.tool.backend.users.ms.util.Constants.PASSWORD_REGULAR_EXPRESSION;

/**
 * Utility class for generating secure passwords that match a predefined pattern.
 */
public class PasswordGeneratorUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "@$!%*?&#";
    private static final String ALL_ALLOWED = UPPERCASE + LOWERCASE + DIGITS + SPECIALS;

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 16;
    private static final int MAX_RETRIES = 10;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGULAR_EXPRESSION);

    /**
     * Generates a password that strictly matches the PASSWORD_REGULAR_EXPRESSION.
     * Retries up to MAX_RETRIES times to satisfy the regex pattern.
     */
    public static String generatePassword() {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            int length = MIN_LENGTH + RANDOM.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
            List<Character> passwordChars = new ArrayList<>();

            // Add required characters
            passwordChars.add(getRandomChar(UPPERCASE));
            passwordChars.add(getRandomChar(LOWERCASE));
            passwordChars.add(getRandomChar(DIGITS));
            passwordChars.add(getRandomChar(SPECIALS));

            while (passwordChars.size() < length) {
                passwordChars.add(getRandomChar(ALL_ALLOWED));
            }

            Collections.shuffle(passwordChars);

            String password = buildStringFromChars(passwordChars);

            if (PASSWORD_PATTERN.matcher(password).matches()) {
                return password;
            }
        }

        throw new IllegalStateException("Unable to generate a valid password after " + MAX_RETRIES + " attempts.");
    }

    private static char getRandomChar(String chars) {
        return chars.charAt(RANDOM.nextInt(chars.length()));
    }

    private static String buildStringFromChars(List<Character> chars) {
        StringBuilder sb = new StringBuilder(chars.size());
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
