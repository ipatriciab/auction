package com.sda.auction.controller;

import com.sda.auction.dto.BidDto;
import com.sda.auction.dto.ProductDto;
import com.sda.auction.dto.UserHeaderDto;
import com.sda.auction.service.BidService;
import com.sda.auction.service.ProductService;
import com.sda.auction.service.UserService;
import com.sda.auction.validator.BidValidator;
import com.sda.auction.validator.GenericValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class HomeController {


    // == fields ==
    private final ProductService productService;
    private final GenericValidator genericValidator;
    private final BidValidator bidValidator;
    private final BidService bidService;
    private final UserService userService;
    private static final String USER_HEADER_DTO = "userHeaderDto";
    private static final String REDIRECT_HOME = "redirect:/home";

    // == constructor ==
    @Autowired
    public HomeController(ProductService productService, GenericValidator genericValidator,
                          BidValidator bidValidator, BidService bidService, UserService userService) {
        this.productService = productService;
        this.genericValidator = genericValidator;
        this.bidValidator = bidValidator;
        this.bidService = bidService;
        this.userService = userService;
    }

    // == mapping methods ==
    // Home Page
    @GetMapping("/home")
    public String getHomePage(Model model, Authentication authentication) {
        log.info("getHomePage called");

        List<ProductDto> productDtoList = productService.getActiveProductDtoList(authentication.getName()); // we call a service to receive a productDtoList for active products
        model.addAttribute("productDtoList", productDtoList);

        List<ProductDto> futureProductDtoList = productService.getFutureProductDtoList(authentication.getName()); // we call a service to receive a productDtoList for future active products
        model.addAttribute("futureProductDtoList", futureProductDtoList);

        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute(USER_HEADER_DTO, userHeaderDto);

        return "home";
    }


    // Product Page
    @GetMapping("/viewProduct/{productId}")
    public String getViewProduct(Model model, @PathVariable(value = "productId") String productId,
                                 Authentication authentication) throws ParseException {
        if (genericValidator.isNotPositiveInteger(productId)) {
            return REDIRECT_HOME;
        }
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId, authentication.getName());
        if (!optionalProductDto.isPresent()) {
            return REDIRECT_HOME;
        }
        ProductDto productDto = optionalProductDto.get();

        model.addAttribute("product", productDto);
        model.addAttribute("bidDto", new BidDto());

        String endBiddingTime = optionalProductDto.get().getEndBiddingTime();
        model.addAttribute("endDate", productService.getParse(endBiddingTime));

        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute(USER_HEADER_DTO, userHeaderDto);

        log.info("Product viewed");
        return "viewProduct";
    }

    // this method returns a list of product or products we searched for based on a keyword
    @GetMapping("/search")
    public String searchProduct(Model model, @Param("keyword") String keyword, Authentication authentication) {

        List<ProductDto> searchedProductDtoList = productService.search(keyword, authentication.getName());
        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        log.info(userHeaderDto.toString());
        if (searchedProductDtoList.isEmpty()) {
            return REDIRECT_HOME;
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchResult", searchedProductDtoList);
        model.addAttribute(USER_HEADER_DTO, userHeaderDto);

        log.info("searchProduct called");
        return "searchResult";
    }

    // this method will post a bid on a product
    @PostMapping("/viewProduct/{productId}")
    public String postBid(Model model, @PathVariable(value = "productId") String productId,
                          BidDto bidDto, BindingResult bindingResult, Authentication authentication)
            throws ParseException {

        String loggedUserEmail = authentication.getName();
        bidValidator.validate(productId, bidDto, bindingResult);

        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId, authentication.getName());
        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        String endBiddingTime = optionalProductDto.get().getEndBiddingTime();

        if (bindingResult.hasErrors()) {

            model.addAttribute("endDate", productService.getParse(endBiddingTime));
            model.addAttribute(USER_HEADER_DTO, userHeaderDto);
            model.addAttribute("bidDto", bidDto);
            model.addAttribute("product", optionalProductDto.get());
            return "viewProduct";
        }
        log.info("Bid placed");
        bidService.placeBid(bidDto, productId, loggedUserEmail);
        return REDIRECT_HOME;
    }
}
