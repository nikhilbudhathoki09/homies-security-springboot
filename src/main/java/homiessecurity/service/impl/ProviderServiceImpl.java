package homiessecurity.service.impl;


import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.entities.*;
import homiessecurity.exceptions.CustomCommonException;
import homiessecurity.exceptions.ResourceAlreadyExistsException;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.repository.ProviderRepository;
import homiessecurity.service.CategoryService;
import homiessecurity.service.EmailSenderService;
import homiessecurity.service.LocationService;
import homiessecurity.service.ProviderService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepository providerRepo;
    private final CategoryService categoryService;

    private final LocationService locationService;
    private final EmailSenderService emailSender;
    private final ModelMapper modelMapper;


    @Autowired
    public ProviderServiceImpl(ProviderRepository providerRepo, CategoryService categoryService,
                               ModelMapper modelMapper,EmailSenderService emailSender
                                ,LocationService locationService){
        this.providerRepo = providerRepo;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.emailSender = emailSender;
        this.locationService = locationService;

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
                .password(register.getPassword())
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
    public ServiceProvider getServiceProviderById(Integer providerId) {
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
        ServiceProvider provider = getServiceProviderById(providerId);
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
    public List<ServiceCategory> getAllCategoriesById(Integer providerId) {
        ServiceProvider provider = getServiceProviderById(providerId);
        return provider.getCategories();
    }

    @Override
    public List<Services> getAllServicesById(Integer providerId) {
        ServiceProvider provider = getServiceProviderById(providerId);
        return provider.getAllServices();
    }

    @Override
    public ServiceProvider addCategoryById(Integer providerId, Integer categoryId) {
        List<ServiceCategory> categories = getAllCategoriesById(providerId);
        if(categories.size() >= 3){
            throw new CustomCommonException("You can only choose 3 categories");
        }else{
            ServiceCategory category = categoryService.getCategoryById(categoryId);
            ServiceProvider provider = getServiceProviderById(providerId);
            categories.add(category); 
            provider.setCategories(categories);    
            return providerRepo.save(provider);  
        }
    }

    @Override
    public void updateService(ServiceProvider provider) {
        providerRepo.save(provider);
    }


}
