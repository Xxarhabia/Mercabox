package com.msara.service.impl;

import com.msara.controller.dto.request.AuthRegisterRequest;
import com.msara.controller.dto.response.AuthResponse;
import com.msara.domain.entity.RoleEntity;
import com.msara.domain.entity.UserEntity;
import com.msara.domain.repository.RoleRepository;
import com.msara.domain.repository.UserRepository;
import com.msara.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserEntity userEntity;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userRepository.findUserEntityByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }

    public void requestAdminRegister(AuthRegisterRequest authRegisterRequest) {
        String username = authRegisterRequest.username();
        String email = authRegisterRequest.email();
        String password = authRegisterRequest.password();
        String confirmPassword = authRegisterRequest.confirmPassword();
        List<String> roles = authRegisterRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntitySet = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnum(roles));
        if (roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist");
        }

        if (!password.equals(confirmPassword)) {
            throw new BadCredentialsException("Passwords are not the same");
        }

        //TODO enviar correo

    }

}
