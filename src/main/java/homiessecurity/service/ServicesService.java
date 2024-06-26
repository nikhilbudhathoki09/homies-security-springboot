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

    public List<ServicesDto> getServicesByCategoryWithRatings(Integer categoryId);

    public List<ServicesDto> getServicesByCategoryIdAndLocationId(Integer categoryId, Integer locationId);

    public List<ServicesDto> getServicesByLocationId(Integer locationId);

    public List<ServicesDto> getServicesByPriceRangeAndCategoryId(Double min, Double max, Integer categoryId);

}
