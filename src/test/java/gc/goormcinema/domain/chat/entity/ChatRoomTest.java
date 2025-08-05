package gc.goormcinema.domain.chat.entity;

import gc.goormcinema.domain.user.entity.User;
import gc.goormcinema.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatRoom 엔티티 테스트")
class ChatRoomTest {

    private User testUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@example.com")
                .name("테스트 사용자")
                .phone("010-1234-5678")
                .role(UserRole.USER)
                .isSocial(false)
                .build();

        adminUser = User.builder()
                .email("admin@example.com")
                .name("관리자")
                .phone("010-9876-5432")
                .role(UserRole.ADMIN)
                .isSocial(false)
                .build();
    }

    @Test
    @DisplayName("ChatRoom 엔티티 생성 테스트")
    void createChatRoom() {
        // given
        String roomCode = "ROOM_001";
        String title = "고객 문의";

        // when
        ChatRoom chatRoom = ChatRoom.builder()
                .roomCode(roomCode)
                .title(title)
                .roomType(ChatRoomType.INQUIRY)
                .roomStatus(ChatRoomStatus.ACTIVE)
                .createdBy(testUser)
                .totalMessageCount(0)
                .build();

        // then
        assertThat(chatRoom.getRoomCode()).isEqualTo(roomCode);
        assertThat(chatRoom.getTitle()).isEqualTo(title);
        assertThat(chatRoom.getRoomType()).isEqualTo(ChatRoomType.INQUIRY);
        assertThat(chatRoom.getRoomStatus()).isEqualTo(ChatRoomStatus.ACTIVE);
        assertThat(chatRoom.getCreatedBy()).isEqualTo(testUser);
        assertThat(chatRoom.getTotalMessageCount()).isEqualTo(0);
        assertThat(chatRoom.isActive()).isTrue();
    }

    @Test
    @DisplayName("채팅방 닫기 테스트")
    void closeRoom() {
        // given
        ChatRoom chatRoom = ChatRoom.builder()
                .roomCode("ROOM_002")
                .title("테스트 채팅방")
                .roomType(ChatRoomType.INQUIRY)
                .roomStatus(ChatRoomStatus.ACTIVE)
                .createdBy(testUser)
                .build();

        LocalDateTime beforeClose = LocalDateTime.now();

        // when
        chatRoom.closeRoom(adminUser);

        // then
        assertThat(chatRoom.getRoomStatus()).isEqualTo(ChatRoomStatus.CLOSED);
        assertThat(chatRoom.getClosedBy()).isEqualTo(adminUser);
        assertThat(chatRoom.getClosedAt()).isAfter(beforeClose);
        assertThat(chatRoom.isActive()).isFalse();
    }

    @Test
    @DisplayName("마지막 메시지 업데이트 테스트")
    void updateLastMessage() {
        // given
        ChatRoom chatRoom = ChatRoom.builder()
                .roomCode("ROOM_003")
                .title("메시지 테스트")
                .roomType(ChatRoomType.INQUIRY)
                .roomStatus(ChatRoomStatus.ACTIVE)
                .createdBy(testUser)
                .totalMessageCount(5)
                .build();

        Long messageId = 123L;
        LocalDateTime beforeUpdate = LocalDateTime.now();

        // when
        chatRoom.updateLastMessage(messageId);

        // then
        assertThat(chatRoom.getLastMessageId()).isEqualTo(messageId);
        assertThat(chatRoom.getLastMessageAt()).isAfter(beforeUpdate);
        assertThat(chatRoom.getTotalMessageCount()).isEqualTo(6);
    }

    @Test
    @DisplayName("관리자 할당 테스트")
    void assignAdmin() {
        // given
        ChatRoom chatRoom = ChatRoom.builder()
                .roomCode("ROOM_004")
                .title("관리자 할당 테스트")
                .roomType(ChatRoomType.INQUIRY)
                .roomStatus(ChatRoomStatus.ACTIVE)
                .createdBy(testUser)
                .build();

        // when
        chatRoom.assignAdmin(adminUser);

        // then
        assertThat(chatRoom.getAssignedAdmin()).isEqualTo(adminUser);
    }

    @Test
    @DisplayName("채팅방 활성 상태 확인 테스트")
    void isActiveTest() {
        // given
        ChatRoom activeChatRoom = ChatRoom.builder()
                .roomCode("ROOM_005")
                .title("활성 채팅방")
                .roomStatus(ChatRoomStatus.ACTIVE)
                .createdBy(testUser)
                .build();

        ChatRoom closedChatRoom = ChatRoom.builder()
                .roomCode("ROOM_006")
                .title("닫힌 채팅방")
                .roomStatus(ChatRoomStatus.CLOSED)
                .createdBy(testUser)
                .build();

        // when & then
        assertThat(activeChatRoom.isActive()).isTrue();
        assertThat(closedChatRoom.isActive()).isFalse();
    }
}