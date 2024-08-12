package com.app.services.impl;

import com.app.dto.GeometryLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocationMatcher {

    private static final double EARTH_RADIUS = 6371; // Earth radius in kilometers
    private static final double THRESHOLD = 0.05; // Threshold distance in kilometers

    // Haversine formula to calculate distance
    public static double haversine(GeometryLocation location1, GeometryLocation location2) {
        double lat1 = location1.getLat();
        double lat2 = location2.getLat();
        double lon1 = location1.getLng();
        double lon2 = location2.getLng();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance in kilometers
    }

    public static boolean isAddressMatching(GeometryLocation location1, GeometryLocation location2) {

        double distance = haversine(location1, location2);

        // change to 2 decimal places
        distance = Math.round(distance * 100) / 100.0;
        log.info("Distance between the locations: {}km", distance);
        return distance <= THRESHOLD;
    }

}
