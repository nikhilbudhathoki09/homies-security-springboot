package homiessecurity.controllers;

import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.dtos.Services.AddServiceDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.Services;
import homiessecurity.service.ProviderService;
import homiessecurity.service.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Provider;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/providers")
public class ProviderController {

    private final ProviderService providerService;
    private final ServicesService servicesService;

    public ProviderController(ProviderService providerService, ServicesService servicesService){
        this.providerService = providerService;
        this.servicesService = servicesService;
    }


    @GetMapping("/")
    public ResponseEntity<List<ServiceProvider>> getAllProvider(){
        return new ResponseEntity<List<ServiceProvider>>(this.providerService.getAllProviders(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ProviderRegistrationRequestDto> registerServiceProvider(@RequestBody ProviderRegistrationRequestDto register){
        ProviderRegistrationRequestDto provider = this.providerService.registerServiceProvider(register);
        return new ResponseEntity<ProviderRegistrationRequestDto>(provider, HttpStatus.CREATED);
    }

    @GetMapping("/verified")
    public ResponseEntity<List<ServiceProvider>> getAllVerifiedProvider(){
        return new ResponseEntity<List<ServiceProvider>>(this.providerService.getAllVerifiedProviders(), HttpStatus.OK);
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<ServiceProvider> getProviderById(@PathVariable Integer providerId){
        return new ResponseEntity<ServiceProvider>(this.providerService.getServiceProviderById(providerId), HttpStatus.OK);
    }

    @PutMapping("/{providerId}/status")
    public ResponseEntity<ServiceProvider> updateProviderStatus(@PathVariable Integer providerId, @RequestParam String status){
        return new ResponseEntity<ServiceProvider>(this.providerService.updateProviderStatus(providerId, status), HttpStatus.OK);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ServiceProvider>> getAllApprovedProviders(){
        return new ResponseEntity<List<ServiceProvider>>(this.providerService.getAllApprovedProviders(), HttpStatus.OK);
    }

    @PostMapping("/add_service/{providerId}")
    public ResponseEntity<Services> addServicesToProvider(@ModelAttribute AddServiceDto service, @PathVariable Integer providerId
                                                            ,@RequestParam(value = "serviceImage", required = false) MultipartFile file){
        if(file != null){
            service.setServiceImage(file);
        }
        return new ResponseEntity<Services>(this.servicesService.addService(service,providerId), HttpStatus.OK);
    }

    //sevices controller
    @GetMapping("/{providerId}/services")
    public ResponseEntity<List<Services>> getAllServicesByProvider(@PathVariable Integer providerId) {
        List<Services> services = servicesService.getAllServicesByProvider(providerId);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/services")
    public ResponseEntity<List<Services>> getAllServices() {
        List<Services> services = servicesService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/services/category/{category}")
    public ResponseEntity<List<Services>> getAllServicesByCategory(@PathVariable String category) {
        List<Services> services = servicesService.getAllServicesByCategory(category);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/services/search")
    public ResponseEntity<List<Services>> getSearchedServices(@RequestParam String search) {
        List<Services> services = servicesService.getSearchedServices(search);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }


}
