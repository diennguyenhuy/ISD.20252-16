package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.product.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class ProductTypeRepositoryImpl implements ProductTypeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countByType(Class<? extends Product> subclass) {
        return entityManager.unwrap(Session.class)
                .createSelectionQuery(
                        "SELECT COUNT(p) FROM " +
                                subclass.getSimpleName() + " p",
                        Long.class
                ).getSingleResult();
    }

    @Override
    public <T extends Product> List<T> getByType(Class<T> subclass) {
        return entityManager.unwrap(Session.class)
                .createSelectionQuery(
                        "SELECT p FROM " + subclass.getSimpleName() + " p",
                        subclass
                ).getResultList();
    }
}
