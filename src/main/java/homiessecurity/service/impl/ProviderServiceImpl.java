package homiessecurity.service.impl;


import homiessecurity.dtos.Providers.ProviderDto;
import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.entities.*;
import homiessecurity.exceptions.CustomCommonException;
import homiessecurity.exceptions.ResourceAlreadyExistsException;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.repository.ProviderRepository;
import homiessecurity.service.CategoryService;
import homiessecurity.service.EmailSenderService;
import homiessecurity.service.LocationService;
import homiessecurity.service.ProviderService;
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


    @Autowired
    public ProviderServiceImpl(ProviderRepository providerRepo, CategoryService categoryService,
                               ModelMapper modelMapper,EmailSenderService emailSender
                                ,LocationService locationService, PasswordEncoder encoder){
        this.providerRepo = providerRepo;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.emailSender = emailSender;
        this.locationService = locationService;
        this.encoder = encoder;

    }

    @Override
    public ProviderRegistrationRequestDto registerServiceProvider(ProviderRegistrationRequestDto register) {
        if(providerRepo.existsByEmail(register.getEmail())){
            throw new ResourceAlreadyExistsException("Email already exists. Try a new one");
        }

        if(providerRepo.existsByPhoneNumber(register.getPhoneNumber())){
            throw new ResourceAlreadyExistsException("PhoneNumber is already  in use. Try a new one ");
        }

//        Locations location = locationService.getLocationById(register.getLocation());

        ServiceProvider provider = ServiceProvider.builder()
                .providerName(register.getProviderName())
                .email(register.getEmail())
                .status(Status.PENDING)
                .phoneNumber(register.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .password(encoder.encode(register.getPassword()))
                .address(register.getAddress())
                .description(register.getDescription())
                .build();

        ServiceProvider requestedProvider = providerRepo.save(provider);
        if(requestedProvider !=null){
            try {
                emailSender.sendHtmlEmail(requestedProvider.getEmail(), requestedProvider.getProviderName(),"Welcome to Homiess");
            } catch (MessagingException e) {
                System.out.println("Error sending email");
                throw new RuntimeException(e);
            }

        }

        return modelMapper.map(requestedProvider, ProviderRegistrationRequestDto.class);
        
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
        provider.setStatus(Status.valueOf(status)); //value from the status to the provider
        provider.setUpdatedAt(LocalDateTime.now());
        providerRepo.save(provider);
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
        providerDto.setCategories(serviceProvider.getCategory());

        return providerDto;
    }

    @Override
    public int verifyProvider(String email) {
        return providerRepo.verifyProvider(email);
    }

    @Override
    public ServiceProvider getRawProviderById(Integer providerId) {
        return providerRepo.findById(providerId).orElseThrow(() ->
                new ResourceNotFoundException("Provider", "providerId", providerId));
    }

    @Override
    public String getCategoryNameById(Integer providerId) {
        ServiceProvider provider = getProviderById(providerId);
        ServiceCategory category = provider.getCategory();
        return category != null ? category.getTitle().toLowerCase() : null;
    }

    public List<ServiceProvider> getProvidersByIds(List<Integer> providerIds) {
        // Use your repository to fetch provider objects by IDs
        return providerRepo.findAllById(providerIds);
    }


    public List<Integer> getSuggestedProviderIds(Integer providerId) {
        String flaskServiceUrl = "http://127.0.0.1:5000/suggest-providers";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"providerId\":" + providerId + "}", headers);

        // Adjusted to expect a response as a Map
        ResponseEntity<Map<String, List<Integer>>> response = restTemplate.exchange(
                flaskServiceUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, List<Integer>>>() {}
        );

        // Extract the list of suggested provider IDs from the response map
        List<Integer> suggestedProviderIds = response.getBody().get("suggested_provider_ids");
        return suggestedProviderIds;
    }


}
