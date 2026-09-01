package com.hust.soict.ict.aims.services.productmanagement.log;

import com.hust.soict.ict.aims.models.entities.audit.ProductActions;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
class ProductActionEventListener {
    private final AuthenticationFacade authenticationFacade;
    private final ProductLogRepository productLogRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(ProductAddedEvent event) {
        ProductLog productLog = ProductActions.CREATE.log(authenticationFacade.getCurrentUser(), event.product());
        productLogRepository.save(productLog);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(ProductStockAdjustEvent event) {
        StockAdjustLog log = ProductActions.STOCK_ADJUST.log(
                authenticationFacade.getCurrentUser(),
                event.product(),
                event.oldStock(),
                event.newStock(),
                event.reason()
        );
        productLogRepository.save(log);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(ProductDeletedEvent event) {
        ProductLog log;
        if (event.product().getStatus() == Product.Status.DELETED) {
            log = ProductActions.DELETE.log(authenticationFacade.getCurrentUser(), event.product());
        } else {
            log = ProductActions.DEACTIVATE.log(authenticationFacade.getCurrentUser(), event.product());
        }

        productLogRepository.save(log);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(ProductsDeletedEvent event) {
        List<ProductLog> productLogs = new ArrayList<>();

        event.products().forEach(p -> {
            ProductLog log;
            if (p.getStatus() == Product.Status.DELETED) {
                log = ProductActions.DELETE.log(authenticationFacade.getCurrentUser(), p);
            } else {
                log = ProductActions.DEACTIVATE.log(authenticationFacade.getCurrentUser(), p);
            }
            productLogs.add(log);
        });

        productLogRepository.saveAll(productLogs);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(ProductReactivatedEvent event) {
        ProductLog log = ProductActions.ACTIVATE.log(authenticationFacade.getCurrentUser(), event.product());
        productLogRepository.save(log);
    }
}
