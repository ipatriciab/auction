package com.sda.auction.validator;

import com.sda.auction.dto.BidDto;
import com.sda.auction.model.Bid;
import com.sda.auction.model.Product;
import com.sda.auction.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Comparator;
import java.util.Optional;

@Service
public class BidValidator {

    // == fields ==
    private final ProductRepository productRepository;
    private static final String BID_DTO = "bidDto";
    private static final String VALUE = "value";

    // == constructor ==
    @Autowired
    public BidValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // == public methods ==
    public void validate(String productId, BidDto bidDto, BindingResult bindingResult) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.valueOf(productId));
        if (!optionalProduct.isPresent()) {
            bindingResult.addError(new FieldError(BID_DTO, VALUE, "Invalid product Id"));
            return;
        }

        validateBidValue(bidDto, bindingResult, optionalProduct.get());

    }

    // == private methods ==
    private void validateBidValue(BidDto bidDto, BindingResult bindingResult, Product product) {

        // if the user input not number will throw this error
        if (isBidValueNotNumber(bidDto)) {
            bindingResult.addError(new FieldError(BID_DTO, VALUE, "This field should be a number."));
            return;
        }

        Optional<Bid> optionalMaxBid = getMaxBid(product);
        int bidDtoValue = Integer.parseInt(bidDto.getValue());
        int productCurrentPrice = product.getStartingPrice(); // variable to store the currentPrice
        int bidStep = product.getMinimumBidStep();

        boolean isError = false;
        String errorMessage = null;

        if (optionalMaxBid.isPresent()) {
            productCurrentPrice = optionalMaxBid.get().getValue();
            if (bidDtoValue <= productCurrentPrice) {
                isError = true;
                errorMessage = "Value is smaller than the last bid!";
            } else if ((bidDtoValue - productCurrentPrice) < bidStep) {
                isError = true;
                errorMessage = "Bid step is smaller than the minimum Bid Step";
            }
        } else if (bidDtoValue < productCurrentPrice) {
            isError = true;
            errorMessage = "Value is smaller than the starting price!";

        }
        if (isError) {
            bindingResult.addError(new FieldError(BID_DTO, VALUE, errorMessage));
        }
    }

    private Optional<Bid> getMaxBid(Product product) {
        return product.getBidList() // gets the BidList
                .stream() // created stream with max method
                .max(Comparator.comparing(Bid::getValue)); // will compare each bid and returns the maximum value
    }

    // validate input so the user always introduces numbers
    private boolean isBidValueNotNumber(BidDto value) {
        try {
            Integer.parseInt(value.getValue());
        } catch (NumberFormatException numberFormatException) {
            return true;
        }
        return false;
    }


}
