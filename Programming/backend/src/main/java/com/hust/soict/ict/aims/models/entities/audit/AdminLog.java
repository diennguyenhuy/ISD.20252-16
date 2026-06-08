package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Immutable @Entity
@Table(name = "admin_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminLog extends AuditLog {
    public enum Action {
        CREATE,
        ACTIVATE,
        DEACTIVATE,
        BLOCK,
        UNBLOCK,
        MODIFY_ROLE,
        RESET_PASSWORD,
        UPDATE_EMAIL
    }

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Action action;

    @ManyToOne
    @JoinColumn(name = "admin_id", updatable = false)
    private User admin;

    @Column(nullable = false, updatable = false)
    private String adminUsername;

    @ManyToOne
    @JoinColumn(name = "affected_user_id", updatable = false)
    private User affectedUser;

    @Column(nullable = false, updatable = false)
    private String affectedUsername;

    public AdminLog(User admin, User affectedUser, Action action) {
        super();
        this.admin = admin;
        this.adminUsername = admin.getUsername();
        this.affectedUser = affectedUser;
        this.affectedUsername = affectedUser.getUsername();
        this.action = action;
    }
}
