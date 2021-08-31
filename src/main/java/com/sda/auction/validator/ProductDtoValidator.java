package com.sda.auction.validator;

import com.sda.auction.dto.ProductDto;
import com.sda.auction.model.enums.ProductCategory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Service
public class ProductDtoValidator {

    // == fields ==
    private static final String PRODUCT_DTO = "productDto";

    // will validate data in controller
    // == public methods ==
    public void validate(ProductDto productDto, BindingResult bindingResult) {

        validateName(productDto.getName(), bindingResult);
        validateDescription(productDto.getDescription(), bindingResult);
        validateCategory(productDto.getCategory(), bindingResult);
        validateStartingPrice(productDto.getStartingPrice(), bindingResult);
        validateMinimumBidStep(productDto.getMinimumBidStep(), bindingResult);

        boolean isStartBiddingTimeValid = validateStartBidingTime(productDto.getStartBiddingTime(), bindingResult);
        boolean isEndBiddingTimeValid = validateEndBidingTime(productDto.getEndBiddingTime(), bindingResult);
        if (isStartBiddingTimeValid && isEndBiddingTimeValid) {
            validateBothBiddingTime(productDto.getStartBiddingTime(), productDto.getEndBiddingTime(), bindingResult);
        }
    }

    // == private methods ==
    private void validateCategory(String category, BindingResult bindingResult) {
        try {
            ProductCategory.valueOf(category);
        } catch (IllegalArgumentException exception) {
            bindingResult.addError(new FieldError(PRODUCT_DTO, "category", "Wrong category."));
        }
    }

    private void validateDescription(String description, BindingResult bindingResult) {
        if (description.isEmpty() || description.length() < 10) {
            bindingResult.addError(
                    new FieldError(PRODUCT_DTO, "description", "Description is too short. Should contain at least 10 characters!"));
        }
    }

    // will validate so the name field cant be empty
    private void validateName(String name, BindingResult bindingResult) {
        if (name.isEmpty()) {
            bindingResult.addError(
                    new FieldError(PRODUCT_DTO, "name", "Name cannot be empty."));
        }
    }

    private void validateStartingPrice(String startingPrice, BindingResult bindingResult) {
        validateFieldAsPositiveInteger("startingPrice", startingPrice, bindingResult);
    }

    private void validateMinimumBidStep(String minimumBidStep, BindingResult bindingResult) {
        validateFieldAsPositiveInteger("minimumBidStep", minimumBidStep, bindingResult);
    }

    // generic method to validate number, will validate the number to be positive and input to always be a integer
    private void validateFieldAsPositiveInteger(String fieldName, String fieldValue, BindingResult bindingResult) {
        try {
            Integer fieldValueAsInteger = Integer.parseInt(fieldValue);
            if (fieldValueAsInteger <= 0) {
                bindingResult.addError(new FieldError(PRODUCT_DTO, fieldName, "This field should be positive."));
            }
        } catch (NumberFormatException numberFormatException) {
            bindingResult.addError(new FieldError(PRODUCT_DTO, fieldName, "This field should be a number."));
        }
    }

    private boolean validateStartBidingTime(String startBiddingTime, BindingResult bindingResult) {
        return isDateTimeFormatValid("startBiddingTime", startBiddingTime, bindingResult);
    }

    private boolean validateEndBidingTime(String endBiddingTime, BindingResult bindingResult) {
        return isDateTimeFormatValid("endBiddingTime", endBiddingTime, bindingResult);
    }

    private boolean isDateTimeFormatValid(String fieldName, String fieldValue, BindingResult bindingResult) {
        try {
            LocalDateTime.parse(fieldValue);
        } catch (DateTimeParseException e) {
            bindingResult.addError(new FieldError(PRODUCT_DTO, fieldName, "Invalid Format"));
            return false;
        }
        return true;
    }

    private void validateBothBiddingTime(String startBiddingTime, String endBiddingTime, BindingResult bindingResult) {
        LocalDateTime start = LocalDateTime.parse(startBiddingTime);
        LocalDateTime end = LocalDateTime.parse(endBiddingTime);
        if (end.isBefore(start) || end.isBefore(LocalDateTime.now())) {
            bindingResult.addError(new FieldError(PRODUCT_DTO, "endBiddingTime", "Should be in the future and after start bidding time."));
        }
        if (start.isBefore(LocalDateTime.now())){
            bindingResult.addError(new FieldError(PRODUCT_DTO, "startBiddingTime", "Start bidding time cannot be in the past."));
        }
    }
}
