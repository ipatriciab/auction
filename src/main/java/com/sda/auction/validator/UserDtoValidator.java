package com.sda.auction.validator;

import com.sda.auction.dto.UserDto;
import com.sda.auction.model.User;
import com.sda.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class UserDtoValidator {

    // == fields ==
    private final UserRepository userRepository;
    private static final String User_DTO = "UserDto";

    // == constructor ==
    @Autowired
    public UserDtoValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // == public methods ==
    //this method will validate the data and checks if the email is already registered
    public void validate(UserDto userDto, BindingResult bindingResult) {

        validateName(userDto.getFirstName(),userDto.getLastName(),bindingResult);
        validateEmail(userDto.getEmail(), bindingResult);
        validateDateOfBirth(userDto.getDateOfBirth(), bindingResult);
        validateIsAdmin(userDto.getIsAdmin(),bindingResult);
        validatePassword(userDto.getPassword(),bindingResult);

    }

    // will validate so the name fields cant be empty
    private void validateName(String firstName, String lastName, BindingResult bindingResult) {
        if (firstName.isEmpty()) {
            bindingResult.addError(
                    new FieldError(User_DTO, "firstName", "First name cannot be empty."));
        }
        if (lastName.isEmpty()) {
            bindingResult.addError(
                    new FieldError(User_DTO, "lastName", "Last name cannot be empty."));
        }
    }


    // will check if the email already exist or validate so the email fields cant be empty
    private void validateEmail(String email, BindingResult bindingResult){
        if (email.isEmpty()) {
            bindingResult.addError(
                    new FieldError(User_DTO, "email", "Email cannot be empty."));
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            bindingResult.addError(
                    new FieldError(User_DTO, "email", "This email is already used."));
        }
    }

    // will validate so the password field cant be empty
    private void validatePassword(String password, BindingResult bindingResult) {
        if (password.isEmpty()) {
            bindingResult.addError(
                    new FieldError(User_DTO, "password", "Password cannot be empty."));
        }
    }
    private boolean validateDateOfBirth(String dateOfBirth, BindingResult bindingResult) {
        return isDateTimeFormatValid("dateOfBirth", dateOfBirth, bindingResult);
    }

    private boolean isDateTimeFormatValid(String fieldName, String fieldValue, BindingResult bindingResult) {
        int year;
        try {
           year  = LocalDate.parse(fieldValue).getYear();
        } catch (DateTimeParseException e) {
            bindingResult.addError(new FieldError(User_DTO, fieldName, "Invalid Format"));
            return false;
        }
        if (year > 2002 ){
            bindingResult.addError(new FieldError(User_DTO, fieldName, "Required age should be at least 18 year old."));
            return false;
        }

        return true;
    }

    // will validate so the isAdmin field cant be unchecked
    private void validateIsAdmin(Boolean isAdmin, BindingResult bindingResult) {
            Boolean testIsAdmin = null;
            if (isAdmin == testIsAdmin){
                bindingResult.addError(
                        new FieldError(User_DTO, "isAdmin", "Please choose your role for the auction"));
            }

        }

}
