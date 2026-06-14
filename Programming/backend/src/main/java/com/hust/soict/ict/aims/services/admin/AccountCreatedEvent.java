package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.models.entities.user.User;

public record AccountCreatedEvent(User createdUser, String temporaryPassword) {
}
