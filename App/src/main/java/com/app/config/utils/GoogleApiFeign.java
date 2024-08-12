package com.app.config.utils;

import com.app.dto.GeoLocationRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GoogleApi", url = "${google.api.url}")
public interface GoogleApiFeign {

    @PostMapping("/geolocation/v1/geolocate")
    ResponseEntity<JsonNode> getGeoLocation(@RequestBody GeoLocationRequest geoLocationRequest, @RequestParam String key);

    @GetMapping("/maps/api/geocode/json")
    ResponseEntity<JsonNode> getGeoCode(@RequestParam String address, @RequestParam String key);
}
