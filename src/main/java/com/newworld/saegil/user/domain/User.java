package com.newworld.saegil.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column
    private String email;

    @Column
    private String profileImageUrl;

    public User(final String name, final String email, final String profileImageUrl) {
        validateName(name);
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidUserException("이름은 필수입니다.");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidUserException("이름은 최대 " + MAX_NAME_LENGTH + "자까지 가능합니다.");
        }
    }
}
