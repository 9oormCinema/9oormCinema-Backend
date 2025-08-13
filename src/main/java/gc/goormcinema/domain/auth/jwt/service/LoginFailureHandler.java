package gc.goormcinema.domain.auth.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gc.goormcinema.global.common.base.BaseResponse;
import gc.goormcinema.global.common.exception.code.BaseCodeDto;
import gc.goormcinema.global.common.exception.code.status.GlobalErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {


        // 이메일이 없는 경우는 필터에서 이미 처리되었으므로, 여기서는 비밀번호 오류만 처리합니다.
        GlobalErrorStatus errorStatus = GlobalErrorStatus.WRONG_PASSWORD;

        response.setStatus(errorStatus.getHttpStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        BaseCodeDto errorCodeDto = errorStatus.getCode();
        BaseResponse<String> failureResponse = BaseResponse.onFailure(errorCodeDto.getCode(), errorCodeDto.getMessage(), request.getRequestURI());

        String jsonResponse = objectMapper.writeValueAsString(failureResponse);
        response.getWriter().write(jsonResponse);

        log.info("[AUTH] : 로그인에 실패했습니다. 에러 메시지 : {}", errorStatus.getMessage());
    }
}
