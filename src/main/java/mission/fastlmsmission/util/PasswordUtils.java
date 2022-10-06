package mission.fastlmsmission.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {

    /**
     * 비밀번호(hashed)와 입력된 비밀번호(plaintext) 일치 여부 확인
     */
    public static boolean equals(String plaintext, String hashed) {
        if (plaintext == null || plaintext.length() < 1) {
            return false;
        }
        if (hashed == null || hashed.length() < 1) {
            return false;
        }
        return BCrypt.checkpw(plaintext, hashed);
    }

    /**
     * 비밀번호 변경
     */
    public static String encryptPassword(String plaintext) {
        if (plaintext == null || plaintext.length() < 1) {
            return "";
        }
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }
}
