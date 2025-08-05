package gc.goormcinema.domain.chat.status;

import gc.goormcinema.global.common.exception.code.BaseCodeDto;
import gc.goormcinema.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatErrorStatus implements BaseCodeInterface {

    // Chat Room 관련 에러
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT4001", "채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "CHAT4002", "이미 종료된 채팅방입니다."),
    CHAT_ROOM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CHAT4003", "채팅방에 접근할 권한이 없습니다."),
    CHAT_ROOM_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CHAT5001", "채팅방 생성에 실패했습니다."),

    // Chat Message 관련 에러
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT4011", "메시지를 찾을 수 없습니다."),
    CHAT_MESSAGE_SEND_FAILED(HttpStatus.BAD_REQUEST, "CHAT4012", "메시지 전송에 실패했습니다."),
    CHAT_MESSAGE_EDIT_DENIED(HttpStatus.FORBIDDEN, "CHAT4013", "메시지를 수정할 권한이 없습니다."),
    CHAT_MESSAGE_DELETE_DENIED(HttpStatus.FORBIDDEN, "CHAT4014", "메시지를 삭제할 권한이 없습니다."),
    CHAT_MESSAGE_CONTENT_EMPTY(HttpStatus.BAD_REQUEST, "CHAT4015", "메시지 내용이 비어있습니다."),
    CHAT_MESSAGE_TOO_LONG(HttpStatus.BAD_REQUEST, "CHAT4016", "메시지가 너무 깁니다."),

    // Chat Participant 관련 에러
    CHAT_PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT4021", "채팅방 참여자를 찾을 수 없습니다."),
    CHAT_PARTICIPANT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CHAT4022", "이미 채팅방에 참여 중입니다."),
    CHAT_PARTICIPANT_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "CHAT4023", "비활성화된 참여자입니다."),

    // Chat Notification 관련 에러
    CHAT_NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT4031", "알림을 찾을 수 없습니다."),
    CHAT_NOTIFICATION_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CHAT5031", "알림 전송에 실패했습니다."),

    // WebSocket 관련 에러
    WEBSOCKET_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CHAT5041", "WebSocket 연결에 실패했습니다."),
    WEBSOCKET_MESSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CHAT5042", "실시간 메시지 전송에 실패했습니다."),

    // 권한 관련 에러
    CHAT_ADMIN_REQUIRED(HttpStatus.FORBIDDEN, "CHAT4051", "관리자 권한이 필요합니다."),
    CHAT_USER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "CHAT4052", "인증이 필요합니다."),

    // 파일 업로드 관련 에러
    CHAT_FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "CHAT4061", "파일 업로드에 실패했습니다."),
    CHAT_FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "CHAT4062", "파일 크기가 제한을 초과했습니다."),
    CHAT_FILE_TYPE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "CHAT4063", "지원하지 않는 파일 형식입니다.");

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public BaseCodeDto getCode() {
        return BaseCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}