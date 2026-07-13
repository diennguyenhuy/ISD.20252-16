package com.hust.soict.ict.aims.constraints;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
class LocationProvider {
    private final Map<String, Set<String>> locations;

    LocationProvider(ObjectMapper objectMapper) throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/static/tinhthanhvietnam_moi.json");

        if (is == null) {
            throw new IOException("Resource not found");
        }

        record LocationData(String name, List<String> wards) {}

        List<LocationData> data = objectMapper.readValue(is, new TypeReference<>() {});

        locations = data.stream().collect(Collectors.toMap(
                LocationData::name,
                l -> new HashSet<>(l.wards())
        ));
    }

    boolean isValidProvince(@NonNull String province) {
        return locations.containsKey(province);
    }

    boolean isValid(@NonNull String province, @NonNull String commune) {
        return isValidProvince(province) && locations.get(province).contains(commune);
    }
}
