package com.msara.service.impl;

import com.msara.controller.dto.request.AuthRegisterRequest;
import com.msara.controller.dto.response.AuthResponse;
import com.msara.domain.entity.RoleEntity;
import com.msara.domain.entity.UserEntity;
import com.msara.domain.repository.RoleRepository;
import com.msara.domain.repository.UserRepository;
import com.msara.service.EmailService;
import com.msara.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = new UserEntity();
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

    public AuthResponse requestAdminRegister(AuthRegisterRequest authRegisterRequest) {
        String verificationToken = UUID.randomUUID().toString();

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

        UserEntity newUser = UserEntity.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .verificationToken(verificationToken)
                .isEnabled(false)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roles(roleEntitySet)
                .build();

        userRepository.save(newUser);

        String verificationLink = "http://localhost:8090/users/verify?token=" + verificationToken;
        emailService.sendEmail(
                newUser.getEmail(),
                "Verifica tu correo",
                "Por favor, verifica tu correo haciendo clic en el siguiente enlace: " + verificationLink);

        return new AuthResponse(username, "Usuario a la espera de validacion", verificationToken);
    }

    public boolean verifyEmail(String token) {
        UserEntity user = userRepository.findByVerificationToken(token);
        if(user != null && !user.isEnabled()) {
            user.setEnabled(true);
            user.setVerificationToken(null);
            return true;
        }
        return false;
    }

}
