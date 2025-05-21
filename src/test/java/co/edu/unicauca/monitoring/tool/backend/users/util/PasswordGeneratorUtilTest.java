package co.edu.unicauca.monitoring.tool.backend.users.util;

import co.edu.unicauca.monitoring.tool.backend.users.ms.util.PasswordGeneratorUtil;
import org.junit.jupiter.api.Test;

import static co.edu.unicauca.monitoring.tool.backend.users.ms.util.Constants.PASSWORD_REGULAR_EXPRESSION;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordGeneratorUtilTest {

    @Test
    void shouldGenerateValidPassword() {
        for (int i = 0; i < 1000; i++) {
            String pwd = PasswordGeneratorUtil.generatePassword();
            assertTrue(pwd.matches(PASSWORD_REGULAR_EXPRESSION));
            assertTrue(pwd.length() >= 8 && pwd.length() <= 16);
        }
    }

}
