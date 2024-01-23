package homiessecurity.service;

import homiessecurity.entities.Locations;

import java.util.List;

public interface LocationService {

    Locations  addLocation(Locations location);

    Locations getLocationById(Integer locationId);

    void deleteLocationById(Integer locationId);

    Locations updateLocationById(Integer locationId, Locations location);

    List<Locations> getAllLocations();
}
