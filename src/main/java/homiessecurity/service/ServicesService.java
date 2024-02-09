package homiessecurity.service;


import homiessecurity.entities.Services;

public interface ServicesService {

//    Services addService(AddServiceDto service, Integer providerId);

    Services getServiceById(Integer serviceId);

    Services addService(Services service, Integer providerId, Integer categoryId);

    
   
}
