package homiessecurity.controllers;

import homiessecurity.entities.Locations;
import homiessecurity.payload.ApiResponse;
import homiessecurity.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationsController {

    private final LocationService locationService;

    @Autowired
    public LocationsController(LocationService locationService){
        this.locationService = locationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Locations>> getAllLocations(){
        return new ResponseEntity<List<Locations>>(locationService.getAllLocations(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Locations> addLocation(@RequestBody Locations location){
        return new ResponseEntity<Locations>(locationService.addLocation(location), HttpStatus.OK);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<Locations> getLocationById(@PathVariable Integer locationId){
        return new ResponseEntity<Locations>(locationService.getLocationById(locationId), HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<ApiResponse> deleteLocationById(@PathVariable Integer locationId){
        locationService.deleteLocationById(locationId);
        ApiResponse response = new ApiResponse("Location Deleted Successfully", true);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<Locations> updateLocationById(@PathVariable Integer locationId, @RequestBody Locations location){
        return new ResponseEntity<Locations>(locationService.updateLocationById(locationId, location), HttpStatus.OK);
    }
}
