package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {

}
