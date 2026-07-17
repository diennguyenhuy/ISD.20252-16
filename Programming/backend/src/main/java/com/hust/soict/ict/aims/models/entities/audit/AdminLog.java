package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Immutable @Entity
@Table(name = "admin_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminLog extends AuditLog {

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserAction action;

    @Column(updatable = false, nullable = false)
    private UUID adminId;

    @Column(nullable = false, updatable = false)
    private String adminUsername;

    @Column(updatable = false, nullable = false)
    private UUID affectedUserId;

    @Column(nullable = false, updatable = false)
    private String affectedUsername;

    public AdminLog(User admin, User affectedUser, UserAction action) {
        super();
        this.adminId = admin.getId();
        this.adminUsername = admin.getUsername();
        this.affectedUserId = affectedUser.getId();
        this.affectedUsername = affectedUser.getUsername();
        this.action = action;
    }
}
