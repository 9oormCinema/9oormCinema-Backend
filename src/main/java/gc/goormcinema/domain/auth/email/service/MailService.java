package gc.goormcinema.domain.auth.email.service;

import gc.goormcinema.domain.user.error.UserAlreadyExistException;
import gc.goormcinema.domain.user.repository.UserRepository;
import gc.goormcinema.global.common.util.RedisUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private int authNumber;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    //임의의 6자리 숫자 변환
    private void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for (int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }

    //mail 전송 양식
    @Async
    public void sendSignUpEmail(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException(); // 이미 존재하면 예외 발생
        }

        makeRandomNumber();

        String setFrom = "9oormCinema";
        String toMail = email;
        String title = "9oormCinema 화원 가입 인증 메일입니다";
        String content =
                "9oormCinema 회원가입을 축하드립니다." +
                        "<br><br>" +
                        "회원가입 인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 제대로 입력해주세요";
        mailSend(setFrom, toMail, title, content);
    }

    //메일 전송
    private void mailSend(String setFrom, String toMail, String title, String content) {
        redisUtil.deleteData(toMail);
        MimeMessage message = javaMailSender.createMimeMessage(); //JavaMailSender 이용해 MimeMessage 객체 생성

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            javaMailSender.send(message);
        } catch (MessagingException | jakarta.mail.MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        redisUtil.setDataExpire(toMail, Integer.toString(authNumber), 60 * 5L);
    }
}