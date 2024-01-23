package homiessecurity.service.impl;

import homiessecurity.entities.Locations;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.repository.LocationRepository;
import homiessecurity.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepo;
    public LocationServiceImpl(LocationRepository locationRepo){
        this.locationRepo = locationRepo;
    }
    @Override
    public Locations addLocation(Locations location) {
        return locationRepo.save(location);
    }

    @Override
    public Locations getLocationById(Integer locationId) {
        Locations location = locationRepo.findById(locationId).orElseThrow(()->
                new ResourceNotFoundException("Location", "LocationId", locationId));
        return location;
    }

    @Override
    public void deleteLocationById(Integer locationId) {
        Locations location = locationRepo.findById(locationId).orElseThrow(()->
                new ResourceNotFoundException("Location", "LocationId", locationId));
        locationRepo.delete(location);
    }

    @Override
    public Locations updateLocationById(Integer locationId, Locations location) {
        Locations existingLocation = locationRepo.findById(locationId).orElseThrow(()->
                new ResourceNotFoundException("Location", "LocationId", locationId));

        if (location.getName() != null) {
            existingLocation.setName(location.getName());
        }
        if (location.getDescription() != null) {
            existingLocation.setDescription(location.getDescription());}
        return locationRepo.save(existingLocation);
    }

    @Override
    public List<Locations> getAllLocations() {
        return locationRepo.findAll();
    }
}
