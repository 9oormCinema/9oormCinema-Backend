package gc.goormcinema.domain.chat.entity;

import gc.goormcinema.domain.user.entity.User;
import gc.goormcinema.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatParticipant 엔티티 테스트")
class ChatParticipantTest {

    private User customerUser;
    private User adminUser;
    private ChatRoom testRoom;

    @BeforeEach
    void setUp() {
        customerUser = User.builder()
                .email("customer@example.com")
                .name("고객")
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

        testRoom = ChatRoom.builder()
                .roomCode("ROOM_001")
                .title("테스트 채팅방")
                .roomType(ChatRoomType.INQUIRY)
                .roomStatus(ChatRoomStatus.ACTIVE)
                .createdBy(customerUser)
                .build();
    }

    @Test
    @DisplayName("ChatParticipant 엔티티 생성 테스트")
    void createChatParticipant() {
        // given
        LocalDateTime joinedAt = LocalDateTime.now();

        // when
        ChatParticipant participant = ChatParticipant.builder()
                .room(testRoom)
                .user(customerUser)
                .participantRole(ParticipantRole.CUSTOMER)
                .joinedAt(joinedAt)
                .isActive(true)
                .unreadCount(0)
                .notificationEnabled(true)
                .build();

        // then
        assertThat(participant.getRoom()).isEqualTo(testRoom);
        assertThat(participant.getUser()).isEqualTo(customerUser);
        assertThat(participant.getParticipantRole()).isEqualTo(ParticipantRole.CUSTOMER);
        assertThat(participant.getJoinedAt()).isEqualTo(joinedAt);
        assertThat(participant.getIsActive()).isTrue();
        assertThat(participant.getUnreadCount()).isEqualTo(0);
        assertThat(participant.getNotificationEnabled()).isTrue();
        assertThat(participant.isCustomer()).isTrue();
        assertThat(participant.isAdmin()).isFalse();
    }

    @Test
    @DisplayName("채팅방 나가기 테스트")
    void leaveRoom() {
        // given
        ChatParticipant participant = ChatParticipant.builder()
                .room(testRoom)
                .user(customerUser)
                .participantRole(ParticipantRole.CUSTOMER)
                .isActive(true)
                .build();

        LocalDateTime beforeLeave = LocalDateTime.now();

        // when
        participant.leaveRoom();

        // then
        assertThat(participant.getIsActive()).isFalse();
        assertThat(participant.getLeftAt()).isAfter(beforeLeave);
    }

    @Test
    @DisplayName("채팅방 재참여 테스트")
    void rejoinRoom() {
        // given
        ChatParticipant participant = ChatParticipant.builder()
                .room(testRoom)
                .user(customerUser)
                .participantRole(ParticipantRole.CUSTOMER)
                .isActive(false)
                .leftAt(LocalDateTime.now().minusHours(1))
                .build();

        // when
        participant.rejoinRoom();

        // then
        assertThat(participant.getIsActive()).isTrue();
        assertThat(participant.getLeftAt()).isNull();
    }

    @Test
    @DisplayName("마지막 읽은 메시지 업데이트 테스트")
    void updateLastReadMessage() {
        // given
        ChatParticipant participant = ChatParticipant.builder()
                .room(testRoom)
                .user(customerUser)
                .participantRole(ParticipantRole.CUSTOMER)
                .unreadCount(5)
                .build();

        Long messageId = 123L;
        LocalDateTime beforeUpdate = LocalDateTime.now();

        // when
        participant.updateLastReadMessage(messageId);

        // then
        assertThat(participant.getLastReadMessageId()).isEqualTo(messageId);
        assertThat(participant.getLastReadAt()).isAfter(beforeUpdate);
        assertThat(participant.getUnreadCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("안읽은 메시지 수 증가 테스트")
    void incrementUnreadCount() {
        // given
        ChatParticipant participant = ChatParticipant.builder()
                .room(testRoom)
                .user(customerUser)
                .participantRole(ParticipantRole.CUSTOMER)
                .unreadCount(3)
                .build();

        // when
        participant.incrementUnreadCount();

        // then
        assertThat(participant.getUnreadCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("알림 설정 토글 테스트")
    void toggleNotification() {
        // given
        ChatParticipant participant = ChatParticipant.builder()
                .room(testRoom)
                .user(customerUser)
                .participantRole(ParticipantRole.CUSTOMER)
                .notificationEnabled(true)
                .build();

        // when
        participant.disableNotification();

        // then
        assertThat(participant.getNotificationEnabled()).isFalse();

        // when
        participant.enableNotification();

        // then
        assertThat(participant.getNotificationEnabled()).isTrue();
    }

    @Test
    @DisplayName("참여자 역할 확인 테스트")
    void participantRoleTest() {
        // given
        ChatParticipant customer = ChatParticipant.builder()
                .room(testRoom)
                .user(customerUser)
                .participantRole(ParticipantRole.CUSTOMER)
                .build();

        ChatParticipant admin = ChatParticipant.builder()
                .room(testRoom)
                .user(adminUser)
                .participantRole(ParticipantRole.ADMIN)
                .build();

        // when & then
        assertThat(customer.isCustomer()).isTrue();
        assertThat(customer.isAdmin()).isFalse();

        assertThat(admin.isCustomer()).isFalse();
        assertThat(admin.isAdmin()).isTrue();
    }

    @Test
    @DisplayName("관리자 참여자 생성 테스트")
    void createAdminParticipant() {
        // when
        ChatParticipant adminParticipant = ChatParticipant.builder()
                .room(testRoom)
                .user(adminUser)
                .participantRole(ParticipantRole.ADMIN)
                .joinedAt(LocalDateTime.now())
                .isActive(true)
                .unreadCount(0)
                .notificationEnabled(true)
                .build();

        // then
        assertThat(adminParticipant.getUser()).isEqualTo(adminUser);
        assertThat(adminParticipant.getParticipantRole()).isEqualTo(ParticipantRole.ADMIN);
        assertThat(adminParticipant.isAdmin()).isTrue();
        assertThat(adminParticipant.isCustomer()).isFalse();
    }
}