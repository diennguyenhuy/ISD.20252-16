package com.hust.soict.ict.aims.constraints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LocationProvider {
    @Getter
    @Setter
    @NoArgsConstructor
    private static class LocationData {
        private String name;
        private List<String> wards;
    }

    private final Map<String, Set<String>> locations;

    public LocationProvider(ObjectMapper objectMapper) throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/static/tinhthanhvietnam_moi.json");

        if (is == null) {
            throw new IOException("Resource not found");
        }

        List<LocationData> data = objectMapper.readValue(is, new TypeReference<>() {});

        locations = data.stream().collect(Collectors.toMap(
                LocationData::getName,
                locationData -> new HashSet<>(locationData.getWards())
        ));
    }

    public boolean isValidProvince(@NonNull String province) {
        return locations.containsKey(province);
    }

    public boolean isValid(@NonNull String province, @NonNull String commune) {
        return isValidProvince(province) && locations.get(province).contains(commune);
    }
}
