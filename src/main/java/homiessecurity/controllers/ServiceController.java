package homiessecurity.controllers;

import homiessecurity.dtos.Services.ServicesDto;
import homiessecurity.service.ServicesService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ServicesDto>> getServicesByCategoryIdAndLocationId(
            @RequestParam Integer categoryId,
            @RequestParam Integer locationId) {

        List<ServicesDto> services = servicesService.getServicesByCategoryIdAndLocationId(categoryId, locationId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ServicesDto>> getServicesByLocationId(@PathVariable Integer locationId) {
        List<ServicesDto> services = servicesService.getServicesByLocationId(locationId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/filterByPrice")
    public ResponseEntity<List<ServicesDto>> getServicesByPriceRangeAndCategory(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max,
            @RequestParam("categoryId") Integer categoryId) {

        List<ServicesDto> services = servicesService.getServicesByPriceRangeAndCategoryId(min, max, categoryId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/sortByRatings")
    public ResponseEntity<List<ServicesDto>> getServicesByCategorySortedByRating(
            @RequestParam("categoryId") Integer categoryId) {
        List<ServicesDto> services = servicesService.getServicesByCategoryWithRatings(categoryId);
        if (services.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(services);
    }


}
