package homiessecurity.service;



import homiessecurity.dtos.Providers.ProviderDto;
import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.Services;
import homiessecurity.payload.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ProviderService {

    public ProviderRegistrationRequestDto registerServiceProvider(ProviderRegistrationRequestDto register);

    public ProviderDto getServiceProviderById(Integer providerId);

    public ServiceProvider getProviderById(Integer providerId);


    public List<ServiceProvider> getAllProviders();

    public List<ServiceProvider> getAllVerifiedProviders();

    public List<ServiceProvider> getAllApprovedProviders();

    public ServiceProvider updateProviderStatus(Integer providerId, String status);

    public ServiceProvider getServiceProviderByEmail(String email);

    public List<ServiceCategory> getAllCategoriesById(Integer providerId);

    List<Services> getAllServicesById(Integer providerId);

    public ServiceProvider addCategoryById(Integer providerId, Integer categoryId);

    public void updateService(ServiceProvider provider);

    public UserDetails loadUserByUsername(String email);

    ApiResponse deleteProviderById(Integer providerId);

    public  ProviderDto mapToProviderDto(ServiceProvider serviceProvider);

    int verifyProvider(String email);


    
}
