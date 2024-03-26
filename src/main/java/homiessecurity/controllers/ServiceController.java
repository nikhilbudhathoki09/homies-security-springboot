package homiessecurity.controllers;

import homiessecurity.dtos.Services.ServicesDto;
import homiessecurity.service.ServicesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServicesService servicesService;

    public ServiceController(ServicesService servicesService){
        this.servicesService = servicesService;
    }


    @GetMapping("/filter-services/")
    public List<ServicesDto> getServicesByCategoryIdAndLocationId(@RequestParam Integer categoryId, @RequestParam Integer locationId) {
        return servicesService.getServicesByCategoryIdAndLocationId(categoryId, locationId);
    }

    @GetMapping("/location/{locationId}")
    public List<ServicesDto> getServicesByLocationId(@PathVariable Integer locationId) {
        return servicesService.getServicesByLocationId(locationId);
    }


}
