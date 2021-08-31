package com.sda.auction.controller;

import com.sda.auction.repository.ProductRepository;
import com.sda.auction.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class HomeControllerTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductService productServiceMock;

    @Mock
    private ProductRepository productRepositoryMock;

    @Test
    void getHomePage() {


    }

    @Test
    void getViewProduct() {
    }

    @Test
    void searchProduct() {
    }

    @Test
    void postBid() {
    }
}