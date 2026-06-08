package com.hust.soict.ict.aims.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class VersionedEntity {
    @Version
    private Long version;

    @CreationTimestamp @Immutable
    @Column(updatable = false, nullable = false)
    protected Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    protected Instant updatedAt;
}
