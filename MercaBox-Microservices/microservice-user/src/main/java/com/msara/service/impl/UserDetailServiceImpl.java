package com.msara.service.impl;

import com.msara.config.RabbitMQConfig;
import com.msara.controller.dto.request.AuthLoginRequest;
import com.msara.controller.dto.request.AuthRegisterRequest;
import com.msara.controller.dto.request.AuthVerifyUserRequest;
import com.msara.controller.dto.response.AuthLoginResponse;
import com.msara.controller.dto.response.AuthRegisterResponse;
import com.msara.domain.entity.RoleEntity;
import com.msara.domain.entity.UserEntity;
import com.msara.domain.event.UserEvent;
import com.msara.domain.repository.RoleRepository;
import com.msara.domain.repository.UserRepository;
import com.msara.service.EmailService;
import com.msara.utils.AuthUtils;
import com.msara.utils.JwtUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
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
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
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

    public AuthRegisterResponse requestRegister(AuthRegisterRequest authRegisterRequest) {
        int verificationCode = authUtils.generateVerificationCode();

        String username = authRegisterRequest.username();
        String email = authRegisterRequest.email();
        String password = authRegisterRequest.password();
        String confirmPassword = authRegisterRequest.confirmPassword();
        List<String> roles = authRegisterRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntitySet = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roles));
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
                .verificationCode(verificationCode)
                .isEnabled(false)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roles(roleEntitySet)
                .build();

        userRepository.save(newUser);

        UserEvent event = new UserEvent(newUser.getId(), newUser.getUsername(), newUser.getEmail());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "usuario.creado", event);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        newUser.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        newUser.getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword(), authorities);
        String authToken = jwtUtils.createToken(authentication);

        emailService.sendEmail(
                newUser.getEmail(),
                "Verifica tu correo",
                "Por favor, introduzca el siguiente codigo: " + verificationCode);

        return new AuthRegisterResponse(username, "Usuario a la espera de validacion", authToken, String.valueOf(verificationCode));
    }

    public boolean verifyEmail(AuthVerifyUserRequest authVerifyUserRequest) {
        UserEntity user = userRepository.findUserEntityByEmail(authVerifyUserRequest.email()).orElseThrow();
        if (!user.isEnabled() && authVerifyUserRequest.verificationCode() == user.getVerificationCode()) {
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public AuthLoginResponse loginRequest(AuthLoginRequest authLoginRequest) {
        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = authentication(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);
        return new AuthLoginResponse(email, "User logged successfully", accessToken);
    }

    public Authentication authentication(String email, String password) {
        UserDetails userDetails = loadUserByUsername(email);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid email or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }
        return new UsernamePasswordAuthenticationToken(email, password, userDetails.getAuthorities());
    }

}
