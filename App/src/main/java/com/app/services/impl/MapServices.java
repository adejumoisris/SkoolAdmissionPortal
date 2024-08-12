package com.app.services.impl;

import com.app.config.utils.GoogleApiFeign;
import com.app.dto.GeoLocationRequest;
import com.app.dto.GeometryLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapServices {

    private final ObjectMapper mapper;
    private final GoogleApiFeign googleApiFeign;
    private final LocationMatcher locationMatcher;

    @Value("${google.api.key}")
    private String googleApiKey;

    public ResponseEntity<JsonNode> getGeoLocation(GeoLocationRequest geoLocationRequest) {
        return googleApiFeign.getGeoLocation(geoLocationRequest, googleApiKey);
    }

    public ResponseEntity<Map<String, Object>> getGeoCode(String address) {

        Map<String, Object> responsePayload = new HashMap<>();

        try {
            if (Strings.isBlank(address)) {
                log.error("Address is blank");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            address = convertToUrlFormat(address);

            ResponseEntity<JsonNode> geoCode = googleApiFeign.getGeoCode(address, googleApiKey);

            if (Strings.isBlank(address)) {
                log.error("Error encoding address: {}", address);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Parse the JSON response
            JsonNode apiResponseNode = mapper.readTree(Objects.requireNonNull(geoCode.getBody()).toString());
            log.info("API response: {}", apiResponseNode);
            log.info("API response: {}", apiResponseNode.get("results"));
            JsonNode locationNode = apiResponseNode.path("results").get(0).path("geometry").path("location");
            log.info("Location node: {}", locationNode);

            // Extract latitude and longitude
            double latitude = locationNode.path("lat").asDouble();
            double longitude = locationNode.path("lng").asDouble();
            log.info("Latitude: {}, Longitude: {}", latitude, longitude);

            if (geoCode.getStatusCode().is2xxSuccessful()) {

                responsePayload.put("data", geoCode.getBody());
                return new ResponseEntity<>(responsePayload, HttpStatus.OK);

            } else {

                log.error("Error message: {}", geoCode.getBody().get("error_message").asText());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } catch (Exception e) {

            log.error("Error message: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static String convertToUrlFormat(String address) {
        try {
            return URLEncoder.encode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Error message: {}", e.getMessage());
            return null;
        }

    }

    public ResponseEntity<Map<String, Object>> isAddressMatching(GeometryLocation geoLocation1, GeometryLocation geoLocation2) {

        Map<String, Object> responsePayload = new HashMap<>();

        boolean addressMatching = locationMatcher.isAddressMatching(geoLocation1, geoLocation2);

        if (addressMatching) {
            responsePayload.put("isAddressMatching", true);
        } else {
            responsePayload.put("isAddressMatching", false);
        }

        return new ResponseEntity<>(responsePayload, HttpStatus.OK);
    }
}
