package com.msara.domain.repository;

import com.msara.domain.entity.MailConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailConfigRepository extends JpaRepository<MailConfigEntity, Long> {
}
