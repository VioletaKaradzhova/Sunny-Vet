package com.sunnyvet.main.repository;

import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.domain.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser_Integration() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("hashedpassword");
        user.setEmail("test@sunnyvet.com");
        user.setFullName("Test User");
        user.setRole(Role.USER);

        UserEntity savedUser = userRepository.save(user);
        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertNotNull(foundUser.get().getId());
        assertEquals("testuser", foundUser.get().getUsername());
    }
}