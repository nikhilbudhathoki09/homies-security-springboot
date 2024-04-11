package homiessecurity.service.impl;


import homiessecurity.dtos.Services.AddServiceDto;
import homiessecurity.dtos.Services.ServicesDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.Services;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.repository.ServicesRepository;
import homiessecurity.service.CategoryService;
import homiessecurity.service.CloudinaryService;
import homiessecurity.service.ProviderService;
import homiessecurity.service.ServicesService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicesServiceImpl implements ServicesService {

    private final ServicesRepository servicesRepo;
    private final ModelMapper modelMapper;
    private final ProviderService providerService;
    private final CategoryService categoryService;

    private final CloudinaryService cloudinaryService;

    @Autowired
    public ServicesServiceImpl(ServicesRepository servicesRepo, ModelMapper modelMapper,
                               ProviderService providerService, CategoryService categoryService,
                                CloudinaryService cloudinaryService){
        this.servicesRepo = servicesRepo;
        this.modelMapper = modelMapper;
        this.providerService = providerService;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
    }   

    @Override
    public ServicesDto getServiceDtoById(Integer serviceId) {
        Services service = servicesRepo.findById(serviceId).orElseThrow(() ->
                new ResourceNotFoundException("Service", "serviceId", serviceId));
        return this.modelMapper.map(service, ServicesDto.class);
    }

    @Override
    public Services getServiceById(Integer serviceId) {
        Services service = servicesRepo.findById(serviceId).orElseThrow(() ->
                new ResourceNotFoundException("Service", "serviceId", serviceId));
        return service;
    }

    @Override
    public ServicesDto addService(AddServiceDto service, Integer providerId) {
        ServiceProvider provider = providerService.getProviderById(providerId);
        ServiceCategory category = categoryService.getCategoryByTitle(service.getCategoryName());

        String serviceImage = null;
        if(service.getServiceImage() != null){
            serviceImage = cloudinaryService.uploadImage(service.getServiceImage(), "Services");
        }

        var newService = Services.builder()
                .serviceName(service.getServiceName())
                .description(service.getDescription())
                .perHourRate(service.getPerHourRate())
                .serviceImage(serviceImage)
                .category(category)
                .provider(provider)
                .build();

        //also updating the provider and services collections
        provider.getAllServices().add(newService);
        category.getAllServices().add(newService);
        return this.modelMapper.map(servicesRepo.save(newService), ServicesDto.class);
    }

    @Override
    public Services updateService(Integer serviceId, AddServiceDto service) {
        Services services = getServiceById(serviceId);

        if(service.getServiceName() != null){
            services.setServiceName(service.getServiceName());
        }
        if(service.getDescription() != null){
            services.setDescription(service.getDescription());
        }
        if(service.getPerHourRate() != null){
            services.setPerHourRate(service.getPerHourRate());
        }
        if(service.getServiceImage() != null){
            String serviceImage = cloudinaryService.uploadImage(service.getServiceImage(), "Services");
            services.setServiceImage(serviceImage);
        }
        return servicesRepo.save(services);

    }

    @Override
    public void deleteService(Integer serviceId) {
        Services service = getServiceById(serviceId);
        servicesRepo.delete(service);
    }

    @Override
    public Services getServiceByName(String name) {
        return servicesRepo.findByServiceName(name).orElseThrow(() ->
                new ResourceNotFoundException("Service", "name", name));
    }

    @Override
    public ServicesDto getServiceDtoByName(String name) {
        return this.modelMapper.map(getServiceByName(name), ServicesDto.class);
    }

    @Override
    public List<ServicesDto> getAllServices() {
        List<Services> allServices = servicesRepo.findAll();
        List<ServicesDto> allServicesDto = allServices.stream().map(service ->
                modelMapper.map(service, ServicesDto.class)).toList();
        return allServicesDto;
    }

    @Override
    public List<ServicesDto> getAllServicesByCategory(String category) {
        List<Services> allServices = servicesRepo.findByCategoryTitle(category).orElseThrow(() ->
                new ResourceNotFoundException("Service", "category", category));

        List<ServicesDto> allServicesDto = allServices.stream().map(service ->
                modelMapper.map(service, ServicesDto.class)).toList();

        return allServicesDto;
    }

    @Override
    public List<ServicesDto> getAllServicesByProvider(Integer providerId) {
        List<Services> allServices =  servicesRepo.findByProviderId(providerId).orElseThrow(() ->
                new ResourceNotFoundException("Service", "providerId", providerId));

        List<ServicesDto> allServicesDto = allServices.stream().map(service ->
                modelMapper.map(service, ServicesDto.class)).toList();
        return allServicesDto;
    }


    @Override
    public List<ServicesDto> getSearchedServices(String search) {
        List<Services> searchedServices = servicesRepo.findSearchedServices(search);

        System.out.println(searchedServices.size() + "size of searched services");

        if (searchedServices.isEmpty()) {
            throw new ResourceNotFoundException("Service", "search", search);
        }

        List<ServicesDto> searchedServicesDto = searchedServices.stream()
                .map(service -> modelMapper.map(service, ServicesDto.class))
                .toList();
        return searchedServicesDto;
    }

    @Override
    public List<ServicesDto> getServicesByCategoryIdAndLocationId(Integer categoryId, Integer locationId) {
        List<Services> services = servicesRepo.findByCategoryIdAndLocationId(categoryId, locationId);
        return services.stream()
                .map(service -> modelMapper.map(service, ServicesDto.class))
                .toList();
    }

    public List<ServicesDto> getServicesByLocation(String location) {
        List<Services> services = servicesRepo.findServicesByProviderLocation(location);
        return services.stream()
                .map(service -> modelMapper.map(service, ServicesDto.class))
                .toList();
    }

    @Override
    public List<ServicesDto> getServicesByLocationId(Integer locationId) {
        List<Services> services = servicesRepo.findServicesByProviderLocationId(locationId);
        return services.stream()
                .map(service -> modelMapper.map(service, ServicesDto.class))
                .toList();
    }



    @Override
    public List<ServicesDto> getServicesByPriceRangeAndCategoryId(Double min, Double max, Integer categoryId) {
        List<Services> services = servicesRepo.findByPerHourRateBetweenAndCategory(min, max, categoryId);
        return services.stream()
                .map(service -> modelMapper.map(service, ServicesDto.class))
                .collect(Collectors.toList());
    }


    public List<ServicesDto> getServicesByCategoryWithRatings(Integer categoryId) {
        List<Services> services = servicesRepo.findByCategoryIdOrderByProviderRatingDesc(categoryId);
        return services.stream()
                .map(service -> modelMapper.map(service, ServicesDto.class))
                .collect(Collectors.toList());
    }


}
