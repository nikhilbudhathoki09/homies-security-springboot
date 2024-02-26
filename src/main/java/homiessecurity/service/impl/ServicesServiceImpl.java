package homiessecurity.service.impl;


import homiessecurity.dtos.Services.AddServiceDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.Services;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.repository.ServicesRepository;
import homiessecurity.service.CategoryService;
import homiessecurity.service.CloudinaryService;
import homiessecurity.service.ProviderService;
import homiessecurity.service.ServicesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Services getServiceById(Integer serviceId) {
        Services service = servicesRepo.findById(serviceId).orElseThrow(() ->
                new ResourceNotFoundException("Service", "serviceId", serviceId));
        return service;
    }

    @Override
    public Services addService(AddServiceDto service, Integer providerId) {
        ServiceProvider provider = providerService.getServiceProviderById(providerId);
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
        return servicesRepo.save(newService);
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
        if(Double.isNaN(service.getPerHourRate())){
            services.setPerHourRate(service.getPerHourRate());
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
    public List<Services> getAllServices() {
        return servicesRepo.findAll();
    }

    @Override
    public List<Services> getAllServicesByCategory(String category) {
        return servicesRepo.findByCategoryTitle(category).orElseThrow(() ->
                new ResourceNotFoundException("Service", "category", category));
    }

    @Override
    public List<Services> getAllServicesByProvider(Integer providerId) {
        return servicesRepo.findByProviderId(providerId).orElseThrow(() ->
                new ResourceNotFoundException("Service", "providerId", providerId));
    }

    @Override
    public List<Services> getSearchedServices(String search) {
        return servicesRepo.findByServiceNameContainingIgnoreCase(search).orElseThrow(() ->
                new ResourceNotFoundException("Service", "search", search));
    }




}
