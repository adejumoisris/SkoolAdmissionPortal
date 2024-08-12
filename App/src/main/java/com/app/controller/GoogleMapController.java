package com.app.controller;

import com.app.dto.GeometryLocation;
import com.app.services.impl.MapServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/google")
@RequiredArgsConstructor
public class GoogleMapController {

    private final MapServices mapServices;
    private final ObjectMapper mapper;

    @GetMapping("/geocode")
    public ResponseEntity<Map<String, Object>> getGeoCode(@RequestBody String address) {
        return mapServices.getGeoCode(address);
    }

    @PostMapping("/location")
    public ResponseEntity<Map<String, Object>> getLocation(@RequestBody ObjectNode locationRequest) throws JsonProcessingException {

        // Get the Geolocation string
        String geoJsonString = locationRequest.get("Geolocation").asText();

        // Parse the Geolocation JSON string into a JsonNode
        JsonNode geoLocationNode = mapper.readTree(geoJsonString);
        log.info("geoLocationNode: {}", geoLocationNode);

        GeometryLocation geoLocation1 = mapper.convertValue(geoLocationNode, GeometryLocation.class);
        GeometryLocation geoLocation2 = GeometryLocation
                .builder()
                .lat(6.4979917) //6.4979917
                .lng(3.3792907) //3.3792907
                .build();

        return mapServices.isAddressMatching(geoLocation1, geoLocation2);
    }
}
