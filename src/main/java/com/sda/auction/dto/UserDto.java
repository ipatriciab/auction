package com.sda.auction.dto;

import lombok.Data;

@Data
public class UserDto {
    //    DTO - DATA TRANSFER OBJECT - ma ajuta sa imi transport datele din front in back
    // dar in baza salvam entitatea de user, nu userdto
    private String fisrtName;
    private String lastName;
    private String email;
    private String password;
    private Boolean isAdmin;

//    lombo - pentru a nu mai folosi get/set/constructor


}
