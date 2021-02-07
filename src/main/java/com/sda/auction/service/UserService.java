package com.sda.auction.service;

import com.sda.auction.dto.UserDto;
import com.sda.auction.mapper.UserMapper;
import com.sda.auction.model.Role;
import com.sda.auction.model.User;
import com.sda.auction.repository.RoleRepository;
import com.sda.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void register(UserDto userDto) {
        User user = userMapper.map(userDto);
        assignRolesTo(user, userDto);
        encodePasswordFor(user);
        userRepository.save(user);
    }

    private void encodePasswordFor(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }

    private void assignRolesTo(User user, UserDto userDto) {

        Optional<Role> optionalRole;
        if(userDto.getIsAdmin()){
            optionalRole = roleRepository.findByName("ROLE_ADMIN");
        }else {
            optionalRole = roleRepository.findByName("ROLE_PARTICIPANT");
        }

        if(optionalRole.isPresent()){
            Role role = optionalRole.get();
            user.setRole(role);
        }

    }


}
