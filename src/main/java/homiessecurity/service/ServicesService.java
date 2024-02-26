package homiessecurity.service;


import homiessecurity.dtos.Services.AddServiceDto;
import homiessecurity.entities.Services;

import java.util.List;

public interface ServicesService {
    Services getServiceById(Integer serviceId);

    Services addService(AddServiceDto service, Integer providerId);

    Services updateService(Integer serviceId, AddServiceDto service);

    void deleteService(Integer serviceId);

    Services getServiceByName(String name);

    List<Services> getAllServices();

    List<Services> getAllServicesByCategory(String category);

    List<Services> getAllServicesByProvider(Integer providerId);

    List<Services> getSearchedServices(String search);





    
   
}
