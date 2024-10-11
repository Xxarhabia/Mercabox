package com.msara.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "mail_config")
public class MailConfigEntity {

    @Id
    private Long id;
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean smtpAuth;
    private boolean starttlsEnable;
}
