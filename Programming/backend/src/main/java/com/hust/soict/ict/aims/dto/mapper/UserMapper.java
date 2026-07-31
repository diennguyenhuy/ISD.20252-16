package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.UserResponse;
import com.hust.soict.ict.aims.models.entities.user.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
class UserMapper extends AbstractMapper<User, UserResponse> {

    UserMapper() {
        super(User.class, UserResponse.class, UserResponse::new);
    }

    @Override
    protected void map(User source, UserResponse target) {
        target.setId(source.getId());
        target.setUsername(source.getUsername());
        target.setEmail(source.getEmail());
        target.setActive(source.isActive());
        target.setBlocked(source.isBlocked());
        target.setMustChangePassword(source.isMustChangePassword());
        target.setRoles(source.getRoles().stream().map(User.Role::name).collect(Collectors.toSet()));
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }
}
