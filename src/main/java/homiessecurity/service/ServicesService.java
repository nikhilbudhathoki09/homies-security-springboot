package homiessecurity.service;


import homiessecurity.dtos.Services.AddServiceDto;
import homiessecurity.dtos.Services.ServicesDto;
import homiessecurity.entities.Services;

import java.util.List;
import java.util.Optional;

public interface ServicesService {
    Services getServiceById(Integer serviceId);

    public ServicesDto getServiceDtoById(Integer serviceId);

    ServicesDto addService(AddServiceDto service, Integer providerId);

    Services updateService(Integer serviceId, AddServiceDto service);

    void deleteService(Integer serviceId);

    Services getServiceByName(String name);

    ServicesDto getServiceDtoByName(String name);

    List<ServicesDto> getAllServices();

    List<ServicesDto> getAllServicesByCategory(String category);

    List<ServicesDto> getAllServicesByProvider(Integer providerId);

    List<ServicesDto> getSearchedServices(String search);

    List<ServicesDto> filterServicesByCategory(String category);

    List<ServicesDto> getTopRatedServices();

    List<ServicesDto> getTopRatedServicesByCategory(String category);

    List<ServicesDto> getServicesByPriceRange(Double min, Double max);

    List<ServicesDto> getServicesByProviderName(String name);







    
   
}
