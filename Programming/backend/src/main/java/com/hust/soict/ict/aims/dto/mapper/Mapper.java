package com.hust.soict.ict.aims.dto.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

record MapperKey<S, T>(Class<S> sourceType, T targetType) {}

@Component
public class Mapper {
    private final Map<MapperKey<?, ?>, AbstractMapper<?, ?>> mappers;

    public Mapper(List<AbstractMapper<?, ?>> mappers) {
        this.mappers = mappers.stream().collect(
                Collectors.toMap(
                        m -> new MapperKey<>(m.getSourceClass(), m.getTargetClass()),
                        Function.identity()
                )
        );
    }

    @SuppressWarnings("unchecked")
    public <S, T> T map(S source, Class<T> responseType) {
        AbstractMapper<S, T> mapper = Optional.ofNullable(
                (AbstractMapper<S, T>) mappers.get(new MapperKey<>(source.getClass(), responseType))
        ).orElseThrow(() -> new UnsupportedOperationException("No mapper to map class " + source.getClass().getName() + " to " + responseType.getName()));

        return mapper.map(source);
    }
}
