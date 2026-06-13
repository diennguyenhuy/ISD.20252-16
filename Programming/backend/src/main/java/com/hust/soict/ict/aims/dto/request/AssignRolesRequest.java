package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

/**
 * Payload for the admin "assign roles" endpoint.
 * Replaces the entire role set of the target user.
 */
@Data
public class AssignRolesRequest {
    @NotEmpty
    private Set<User.Role> roles;
}
