package homiessecurity.service.impl;


import homiessecurity.entities.Services;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.repository.ServicesRepository;
import homiessecurity.service.ServicesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicesServiceImpl implements ServicesService {

    private final ServicesRepository servicesRepo;
    private final ModelMapper modelMapper;
//    private final ServiceProviderService providerService;

    @Autowired
    public ServicesServiceImpl(ServicesRepository servicesRepo, ModelMapper modelMapper) {
        this.servicesRepo = servicesRepo;
        this.modelMapper = modelMapper;
//        this.providerService = providerService;
    }   

//    @Override
//    public Services addService(AddServiceDto service, Integer providerId) { //for now only returning the service not the dto
//        Services newService = modelMapper.map(service, Services.class);
//        newService.setProvider(providerService.getServiceProviderById(providerId));
//        return servicesRepo.save(newService);
//
//    }

    @Override
    public Services getServiceById(Integer serviceId) {
        Services service = servicesRepo.findById(serviceId).orElseThrow(() -> new ResourceNotFoundException("Service", "serviceId", serviceId));
        return service;
    }

    @Override
    public Services addService(Services service, Integer providerId, Integer categoryId) {


        return null;
    }


}
