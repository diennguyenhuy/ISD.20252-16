package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.models.entities.audit.ProductLog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductLogging {
    ProductLog.Action action();
}
