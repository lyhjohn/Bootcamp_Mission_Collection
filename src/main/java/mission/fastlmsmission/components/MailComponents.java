package mission.fastlmsmission.components;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class MailComponents {

    private final JavaMailSender javaMailSender;

    public void sendMailTest() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("dladygks506@naver.com");
        msg.setSubject("메시지 테스트");
        msg.setText("나에게 보내는 테스트 메일");
        // setFrom은 yml 파일에 세팅돼있음
        javaMailSender.send(msg);
    }

    public boolean sendMail(String mail, String subject, String text) {
        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                        mimeMessage, true, "UTF-8"
                );
                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true); // html 태그 사용 가능하게 해줌
            }
        };
        try {
            javaMailSender.send(msg);
            result = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
