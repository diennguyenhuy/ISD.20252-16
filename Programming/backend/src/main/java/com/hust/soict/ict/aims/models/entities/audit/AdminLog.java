package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "admin_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminLog {
    public enum Action {
        CREATE,
        ACTIVATE,
        DEACTIVATE,
        BLOCK,
        UNBLOCK,
        ASSIGN_ROLE,
        RESET_PASSWORD,
        UPDATE_EMAIL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Action action;

    @ManyToOne
    @JoinColumn(name = "admin_id", updatable = false)
    private User admin;

    @ManyToOne(optional = false)
    @JoinColumn(name = "affected_user_id", updatable = false)
    private User affectedUser;

    @Column(updatable = false)
    private Instant timestamp;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        String localTimestamp = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String adminInfo = (admin != null) ? "Administrator #" + admin.getId() : "An administrator";

        builder.append("[ADMIN_LOG ").append(localTimestamp).append("]");
        builder.append("Admin");
        if (admin != null) {
            builder.append(" #").append(admin.getId()).append(" ").append(admin.getUsername());
        }

        String actionName = switch (action) {
            case CREATE -> " created ";
            case ACTIVATE -> " reactivated ";
            case DEACTIVATE -> " deactivated ";
            case BLOCK -> " blocked ";
            case UNBLOCK -> " unblocked ";
            case ASSIGN_ROLE -> " assigned role to ";
            case RESET_PASSWORD -> " reset password of ";
            case UPDATE_EMAIL ->  " updated email of ";
        };
        builder.append("user #").append(affectedUser.getId()).append(" ").append(affectedUser.getUsername());
        return "[ADMIN_LOG #" + id + "] " + adminInfo + " "
                + actionName + " user #" + affectedUser.getId()
                + " at " + localTimestamp;
    }

    public static AdminLog of(
            Action action,
            User admin,
            User affectedUser,
            Instant timestamp
    ) {
        AdminLog l = new AdminLog();

        l.action = action;
        l.admin = admin;
        l.affectedUser = affectedUser;
        l.timestamp = timestamp;

        return l;
    }
}
