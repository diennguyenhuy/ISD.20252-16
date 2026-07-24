package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.user.User;

public enum UserAction {
    CREATE,
    ACTIVATE,
    DEACTIVATE,
    BLOCK,
    UNBLOCK,
    MODIFY_ROLE,
    RESET_PASSWORD,
    UPDATE_EMAIL;

    public AdminLog log(User admin, User affectedUser) {
        return new AdminLog(admin, affectedUser, this);
    }
}
