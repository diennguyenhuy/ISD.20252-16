package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductLogMappers {
    private final Map<Class<? extends ProductLog>, AbstractProductLogMapper<?, ?>> mappers = new HashMap<>();

    public ProductLogMappers(List<AbstractProductLogMapper<?, ?>> mappers) {
        mappers.forEach(m -> this.mappers.put(m.logClass, m));
    }

    @SuppressWarnings("unchecked")
    public <L extends ProductLog, R extends ProductLogResponse> R map(L log) {
        AbstractProductLogMapper<L, R> mapper = (AbstractProductLogMapper<L, R>) mappers.get(log.getClass());
        return mapper.map(log);
    }
}
