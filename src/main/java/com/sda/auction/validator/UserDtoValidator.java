package com.sda.auction.validator;

import com.sda.auction.dto.UserDto;
import com.sda.auction.model.User;
import com.sda.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
public class UserDtoValidator {

    private UserRepository userRepository;

    @Autowired
    public UserDtoValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 2/7/2021 validari pentru campurile firstName, lastName, password 
    public void validate(UserDto userDto, BindingResult bindingResult) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if(optionalUser.isPresent()){
            bindingResult.addError(new FieldError("userDto","email","This email is already used."));
        }
    }
}
