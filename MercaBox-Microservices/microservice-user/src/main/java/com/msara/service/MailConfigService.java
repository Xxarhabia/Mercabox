package com.msara.service;

import com.msara.domain.entity.MailConfigEntity;
import com.msara.domain.repository.MailConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailConfigService {

    @Autowired
    private MailConfigRepository mailConfigRepository;

    public MailConfigEntity getEmailConfig() {
        return mailConfigRepository.findById(1L).orElseThrow(() -> new RuntimeException("Mail config not found"));
    }
}
