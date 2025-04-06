package com.newworld.saegil.user.service;

import com.newworld.saegil.exception.UserNotFoundException;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("ID에 해당하는 사용자를 조회하는 메서드에")
    class Describe_readById {

        @Nested
        @DisplayName("존재하는 사용자 ID가 주어지면")
        class Context_with_existing_user {

            @Test
            void 사용자_정보를_반환한다() {
                // given
                final User savedUser = userRepository.save(new User("홍길동", "hong@example.com", "https://example.com/profile.png"));

                entityManager.flush();
                entityManager.clear();

                // when
                final UserDto actual = userService.readById(savedUser.getId());

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual.id()).isEqualTo(savedUser.getId());
                    softAssertions.assertThat(actual.name()).isEqualTo(savedUser.getName());
                    softAssertions.assertThat(actual.email()).isEqualTo(savedUser.getEmail());
                    softAssertions.assertThat(actual.profileImageUrl()).isEqualTo(savedUser.getProfileImageUrl());
                });
            }
        }

        @Nested
        @DisplayName("존재하지 않는 사용자 ID가 주어지면")
        class Context_with_nonexistent_user {

            @Test
            void UserNotFoundException이_발생한다() {
                // given
                final Long invalidUserId = -999L;

                // when & then
                assertThatThrownBy(() -> userService.readById(invalidUserId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("사용자가 존재하지 않습니다.");
            }
        }
    }
}
