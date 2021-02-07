package com.sda.auction.controller;

import com.sda.auction.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHomepage(){
        return "home";
    }

    @GetMapping("/register")
    public String getRegisterpage(Model model){
        System.out.println("se apeleaza get register");
        //UserDto userDto = new UserDto();
/*        userDto.setFisrtName("First Name");
        userDto.setLastName("Last Name");*/

        model.addAttribute("userDto", new UserDto());

        return "register";
    }

    @PostMapping("/register")
    public String postRegisterpage(){
        return "register";
    }
}
