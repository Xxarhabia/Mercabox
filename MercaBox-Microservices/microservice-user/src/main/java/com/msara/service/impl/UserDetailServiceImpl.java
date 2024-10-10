package com.msara.service.impl;

import com.msara.domain.entity.UserEntity;
import com.msara.domain.repository.UserRepository;
import com.msara.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserEntity userEntity;

    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userRepository.findUserEntityByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        return null;
    }
}
