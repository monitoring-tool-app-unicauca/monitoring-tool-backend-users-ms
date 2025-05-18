package co.edu.unicauca.monitoring.tool.backend.users.ms.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static co.edu.unicauca.monitoring.tool.backend.users.ms.util.Constants.PASSWORD_REGULAR_EXPRESSION;

/**
 * Utility class for generating random passwords that match a specified regular expression.
 */
public class PasswordGeneratorUtil {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "@$!%*?&#";
    private static final String ALL_ALLOWED = UPPERCASE + DIGITS + SPECIALS + "abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom random = new SecureRandom();
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGULAR_EXPRESSION);


    public static String generatePassword() {
        int length = 8 + random.nextInt(9);

        List<Character> passwordChars = new ArrayList<>();

        passwordChars.add(getRandomChar(UPPERCASE));
        passwordChars.add(getRandomChar(DIGITS));
        passwordChars.add(getRandomChar(SPECIALS));

        while (passwordChars.size() < length) {
            passwordChars.add(getRandomChar(ALL_ALLOWED));
        }

        Collections.shuffle(passwordChars);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    private static char getRandomChar(String chars) {
        return chars.charAt(random.nextInt(chars.length()));
    }

    public static boolean isValid(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }
}
