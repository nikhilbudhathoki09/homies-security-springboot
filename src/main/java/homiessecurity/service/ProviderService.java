package homiessecurity.service;



import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.Services;

import java.util.List;

public interface ProviderService {

    public ProviderRegistrationRequestDto registerServiceProvider(ProviderRegistrationRequestDto register);

    public ServiceProvider getServiceProviderById(Integer providerId);

    public List<ServiceProvider> getAllProviders();

    public List<ServiceProvider> getAllVerifiedProviders();

    public ServiceProvider updateProviderStatus(Integer providerId, String status);

    public ServiceProvider getServiceProviderByEmail(String email);

    public List<ServiceCategory> getAllCategoriesById(Integer providerId);

    List<Services> getAllServicesById(Integer providerId);

    public ServiceProvider addCategoryById(Integer providerId, Integer categoryId);




    
}
