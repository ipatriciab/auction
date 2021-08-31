package com.sda.auction.service;

import com.sda.auction.dto.UserDto;
import com.sda.auction.model.User;
import com.sda.auction.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @Test
    @DisplayName("Should work when the user is registered")
    void register() {

        UserDto userDto = new UserDto();
        userDto.setEmail("test");

        User user = new User();
        user.setEmail("test");
        userRepositoryMock.save(user);

        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(Mockito.any());

        Assertions.assertEquals(userDto.getEmail(), user.getEmail());


    }

    @Test
    @Disabled
    void getUserHeaderDto() {


    }
}