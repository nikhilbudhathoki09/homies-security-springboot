package homiessecurity.service;



import homiessecurity.dtos.Providers.ProviderDto;
import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.Services;
import homiessecurity.payload.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProviderService {

    public ProviderRegistrationRequestDto registerServiceProvider(ProviderRegistrationRequestDto register);

    public ProviderDto getServiceProviderById(Integer providerId);

    public ServiceProvider getProviderById(Integer providerId);


    public List<ServiceProvider> getAllProviders();

    public List<ServiceProvider> getAllVerifiedProviders();

    public List<ServiceProvider> getAllApprovedProviders();

    public ServiceProvider updateProviderStatus(Integer providerId, String status);

    public ServiceProvider getServiceProviderByEmail(String email);


    List<Services> getAllServicesById(Integer providerId);


    public void updateService(ServiceProvider provider);

    public UserDetails loadUserByUsername(String email);

    ApiResponse deleteProviderById(Integer providerId);

    public  ProviderDto mapToProviderDto(ServiceProvider serviceProvider);

    int verifyProvider(String email);

    public ServiceProvider getRawProviderById(Integer providerId);

    public String getCategoryNameById(Integer providerId);

    public List<Integer> getSuggestedProviderIds(Integer providerId);

    public List<ServiceProvider> getProvidersByIds(List<Integer> providerIds);


    
}
