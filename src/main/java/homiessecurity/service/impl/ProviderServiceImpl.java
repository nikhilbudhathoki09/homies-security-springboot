package homiessecurity.service.impl;


import homiessecurity.dtos.Providers.ProviderDto;
import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.dtos.Providers.UpdateProviderRequestDto;
import homiessecurity.entities.*;
import homiessecurity.exceptions.CustomCommonException;
import homiessecurity.exceptions.ResourceAlreadyExistsException;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.repository.ProviderRepository;
import homiessecurity.service.*;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class ProviderServiceImpl implements ProviderService, UserDetailsService {
    private final ProviderRepository providerRepo;
    private final CategoryService categoryService;

    private final LocationService locationService;
    private final EmailSenderService emailSender;
    private final ModelMapper modelMapper;

    private final PasswordEncoder encoder;

    @Autowired
    private RestTemplate restTemplate;

    private CloudinaryService cloudinaryService;


    @Autowired
    public ProviderServiceImpl(ProviderRepository providerRepo, CategoryService categoryService,
                               ModelMapper modelMapper,EmailSenderService emailSender
                                ,LocationService locationService,CloudinaryService cloudinaryService,
                               PasswordEncoder encoder){
        this.providerRepo = providerRepo;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.emailSender = emailSender;
        this.locationService = locationService;
        this.encoder = encoder;
        this.cloudinaryService = cloudinaryService;
    }


    public ProviderDto updateServiceProvider(int providerId, UpdateProviderRequestDto updateDto) {
        String imageUrl = null;
        ServiceProvider provider = getProviderById(providerId);

        // Update the fields
        if (updateDto.getProviderName() != null) {
            provider.setProviderName(updateDto.getProviderName());
        }
        if (updateDto.getDescription() != null) {
            provider.setDescription(updateDto.getDescription());
        }
        if (updateDto.getPhoneNumber() != null) {
            provider.setPhoneNumber(updateDto.getPhoneNumber());
        }
        if (updateDto.getAddress() != null) {
            provider.setAddress(updateDto.getAddress());
        }
        if (updateDto.getYearOfExperience() != null) {
            provider.setYearOfExperience(updateDto.getYearOfExperience());
        }
        if (updateDto.getMinServicePrice() != null) {
            provider.setMinServicePrice(updateDto.getMinServicePrice());
        }
        if (updateDto.getMaxServicePrice() != null) {
            provider.setMaxServicePrice(updateDto.getMaxServicePrice());
        }

        if(updateDto.getCategoryId() != null){
            provider.setCategory(categoryService.getCategoryById(updateDto.getCategoryId()));
        }

        if(updateDto.getLocationId() != null){
            provider.setLocation(locationService.getLocationById(updateDto.getLocationId()));
        }

        if(updateDto.getProviderImage() != null){
            imageUrl = cloudinaryService.uploadImage(updateDto.getProviderImage(), "ProviderImages");
            provider.setProviderImage(imageUrl);
        }


        provider.setUpdatedAt(LocalDateTime.now());
        ServiceProvider updatedProvider = providerRepo.save(provider);
        return modelMapper.map(updatedProvider, ProviderDto.class);

    }


    @Override
    public ProviderDto getServiceProviderById(Integer providerId) {
        ServiceProvider provider = this.providerRepo.findById(providerId).orElseThrow(()-> new ResourceNotFoundException(
            "Provider", "ProviderId", providerId));

        return mapToProviderDto(provider);
    }

    @Override
    public ServiceProvider getProviderById(Integer providerId) {
        ServiceProvider provider = this.providerRepo.findById(providerId).orElseThrow(()-> new ResourceNotFoundException(
                "Provider", "ProviderId", providerId));

        return provider;
    }

    @Override
    public ServiceProvider getProviderByEmail(String email) {
        return this.providerRepo.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("Provider", "email", email));
    }


    @Override
    public List<ServiceProvider> getAllProviders() {
        return this.providerRepo.findAll();
    }

    @Override
    public List<ServiceProvider> getAllVerifiedProviders() {
        return this.providerRepo.findVerifiedProviders()
                .orElseThrow(() -> new CustomCommonException("No verified providers found"));
    }

    @Override
    public List<ServiceProvider> getAllApprovedProviders() {
        return this.providerRepo.findAllByStatus(Status.APPROVED)
                .orElseThrow(() -> new ResourceNotFoundException("Provider", "status", Status.APPROVED));
    }

    @Override
    public ServiceProvider updateProviderStatus(Integer providerId, String status) {
        ServiceProvider provider = getProviderById(providerId);
        Status newStatus;

        try {
            newStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        provider.setStatus(newStatus);
        provider.setUpdatedAt(LocalDateTime.now());
        providerRepo.save(provider);

        String subject = switch (newStatus) {
            case APPROVED -> "Service Provider Registration Approved 🎉";
            case REJECTED -> "Service Provider Registration Rejected 😞";
            default -> "Service Provider Registration Update";
        };
        try {
            emailSender.sendStatusChangeEmail(provider.getEmail(), provider.getProviderName(), newStatus, subject);
        } catch (MessagingException e) {
            throw new CustomCommonException(e.getMessage());
        }
        return provider;
    }



    @Override
    public ServiceProvider getServiceProviderByEmail(String email) {
        return this.providerRepo.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("Provider", "email", email));
    }


    @Override
    public List<Services> getAllServicesById(Integer providerId) {
        ServiceProvider provider = getProviderById(providerId);
        return provider.getAllServices();
    }

    @Override
    public void updateService(ServiceProvider provider) {
        providerRepo.save(provider);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.providerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Provider not found with email: " + email));
    }

    @Override
    public ApiResponse deleteProviderById(Integer providerId) {
        ServiceProvider provider = this.providerRepo.findById(providerId).orElseThrow(() ->
                new ResourceNotFoundException("Provider", "providerId", providerId));
        this.providerRepo.delete(provider);
        return new ApiResponse("Provider deleted successfully", true);
    }

    public static ServiceProvider mapToServiceProvider(ProviderRegistrationRequestDto requestDto) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setProviderName(requestDto.getProviderName());
        serviceProvider.setEmail(requestDto.getEmail());
        serviceProvider.setDescription(requestDto.getDescription());
        serviceProvider.setPhoneNumber(requestDto.getPhoneNumber());
        serviceProvider.setAddress(requestDto.getAddress());
        serviceProvider.setPassword(requestDto.getPassword());
        return serviceProvider;
    }

    public ProviderDto mapToProviderDto(ServiceProvider serviceProvider) {
        ProviderDto providerDto = new ProviderDto();
        providerDto.setProviderId(serviceProvider.getProviderId());
        providerDto.setProviderName(serviceProvider.getProviderName());
        providerDto.setEmail(serviceProvider.getEmail());
        providerDto.setDescription(serviceProvider.getDescription());
        providerDto.setPhoneNumber(serviceProvider.getPhoneNumber());
        providerDto.setAddress(serviceProvider.getAddress());
        providerDto.setStatus(serviceProvider.getStatus().toString());
        providerDto.setAllServices(serviceProvider.getAllServices());
        providerDto.setCategory(serviceProvider.getCategory());
        providerDto.setYearOfExperience(serviceProvider.getYearOfExperience());
        providerDto.setMinServicePrice(serviceProvider.getMinServicePrice());
        providerDto.setMaxServicePrice(serviceProvider.getMaxServicePrice());
        providerDto.setLocation(serviceProvider.getLocation());
        return providerDto;
    }

    @Override
    public int verifyProvider(String email) {
        return providerRepo.verifyProvider(email);
    }

    public List<ServiceProvider> getProvidersByIds(List<Integer> providerIds) {
        return providerRepo.findAllById(providerIds);
    }

    @Override
    public ServiceProvider saveProvider(ServiceProvider provider) {
        return providerRepo.save(provider);
    }

    @Override
    public List<Integer> getSuggestedProviderIds(Integer providerId) {
        String flaskServiceUrl = "http://127.0.0.1:5000/suggest-providers";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"providerId\":" + providerId + "}", headers);

        try {
            // Adjusted to expect a response as a Map
            ResponseEntity<Map<String, List<Integer>>> response = restTemplate.exchange(
                    flaskServiceUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, List<Integer>>>() {
                    }
            );

            // Extract the list of suggested provider IDs from the response map
            List<Integer> suggestedProviderIds = response.getBody().get("suggested_provider_ids");
            return suggestedProviderIds;
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Provider not found: " + e.getMessage());
            throw new ResourceNotFoundException("Provider", "providerId", providerId);
        }
    }


}
