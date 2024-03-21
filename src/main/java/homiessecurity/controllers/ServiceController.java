package homiessecurity.controllers;

import homiessecurity.service.ServicesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServicesService serviceService;

    public ServiceController(ServicesService serviceService){
        this.serviceService = serviceService;
    }




}
