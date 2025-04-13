package com.newworld.saegil.authentication.domain;

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
@ToString(of = {"id", "userId", "tokenType", "token"})
@Table(name = "blacklist_token")
public class BlacklistToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(nullable = false)
    private String token;

    public BlacklistToken(final Long userId, final TokenType tokenType, final String token) {
        this.userId = userId;
        this.tokenType = tokenType;
        this.token = token;
    }
}
