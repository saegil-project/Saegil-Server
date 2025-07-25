package com.newworld.saegil.user.domain;

import com.newworld.saegil.authentication.domain.OAuth2Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "name"})
@Table(name = "users")
public class User {

    private static final int MAX_NAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(name = "oauth2_id", nullable = false)
    private String oauth2Id;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth2_type", nullable = false)
    private OAuth2Type oauth2Type;

    @Column(nullable = true)
    private String deviceToken;

    public User(final String name, final String profileImageUrl, final String oauth2Id, final OAuth2Type oauth2Type) {
        validateName(name);
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.oauth2Id = oauth2Id;
        this.oauth2Type = oauth2Type;
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidUserException("이름은 필수입니다.");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidUserException("이름은 최대 " + MAX_NAME_LENGTH + "자까지 가능합니다.");
        }
    }

    public void upsertDeviceToken(final String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void invalidateDeviceToken() {
        this.deviceToken = null;
    }
}
