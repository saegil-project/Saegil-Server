package com.newworld.saegil.user.service;

import org.springframework.stereotype.Service;

import com.newworld.saegil.exception.UserNotFoundException;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto readById(final Long userId) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        return UserDto.from(findUser);
    }
}
