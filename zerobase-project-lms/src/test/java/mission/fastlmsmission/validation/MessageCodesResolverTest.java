package mission.fastlmsmission.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodeField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "parameter", "seq", Integer.class);

        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

        Assertions.assertThat(messageCodes).containsExactly("required.parameter.seq", "required.seq", "required.java.lang.Integer", "required");

    }
}
