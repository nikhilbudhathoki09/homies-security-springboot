package homiessecurity.controllers;

import homiessecurity.dtos.Providers.ProviderDto;
import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.dtos.Providers.UpdateProviderRequestDto;
import homiessecurity.dtos.Services.AddServiceDto;
import homiessecurity.dtos.Services.ServicesDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.Services;
import homiessecurity.service.CloudinaryService;
import homiessecurity.service.ProviderService;
import homiessecurity.service.ServicesService;
import jakarta.validation.Valid;
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

    private final CloudinaryService cloudinaryService;

    public ProviderController(ProviderService providerService,CloudinaryService cloudinaryService,
                              ServicesService servicesService){
        this.providerService = providerService;
        this.servicesService = servicesService;
        this.cloudinaryService = cloudinaryService;
    }


    @GetMapping("/")
    public ResponseEntity<List<ServiceProvider>> getAllProvider(){
        return new ResponseEntity<List<ServiceProvider>>(this.providerService.getAllProviders(), HttpStatus.OK);
    }

    @GetMapping("/verified")
    public ResponseEntity<List<ServiceProvider>> getAllVerifiedProvider(){
        return new ResponseEntity<List<ServiceProvider>>(this.providerService.getAllVerifiedProviders(), HttpStatus.OK);
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<ServiceProvider> getProviderById(@PathVariable Integer providerId){
        return new ResponseEntity<ServiceProvider>(this.providerService.getProviderById(providerId), HttpStatus.OK);
    }

//    @PutMapping("/{providerId}")
//    public ResponseEntity<?> updateServiceProvider(@Valid @PathVariable int providerId,
//                                                   UpdateProviderRequestDto updateDto,
//                                                   @RequestParam(value = "providerImage", required = false) MultipartFile providerImage) {
//        if (providerImage != null ) {
//            updateDto.setProviderImage(providerImage);
//        }
//        ProviderDto updatedProviderDto = providerService.updateServiceProvider(providerId, updateDto);
//        return new ResponseEntity<>(updatedProviderDto, HttpStatus.OK);
//    }

    @PutMapping("/{providerId}")
    public ResponseEntity<?> updateServiceProvider(@PathVariable int providerId,
                                                   @RequestBody UpdateProviderRequestDto updateDto,
                                                   @RequestParam(value = "providerImage", required = false) MultipartFile providerImage) {
        if (providerImage != null) {
            String imageUrl = cloudinaryService.uploadImage(providerImage, "ProviderImages");
            updateDto.setProviderImageUrl(imageUrl); // Set the image URL in the DTO
        }

        System.out.println(updateDto);

        ProviderDto updatedProviderDto = providerService.updateServiceProvider(providerId, updateDto);
        return ResponseEntity.ok(updatedProviderDto);
    }


    @DeleteMapping("/{providerId}")
    public ResponseEntity<String> deleteProviderById(@PathVariable Integer providerId){
        this.providerService.deleteProviderById(providerId);
        return new ResponseEntity<String>("Provider deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{providerId}/update-status")
    public ResponseEntity<ServiceProvider> updateProviderStatus(@PathVariable Integer providerId, @RequestParam String status){
        return new ResponseEntity<ServiceProvider>(this.providerService.updateProviderStatus(providerId, status), HttpStatus.OK);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ServiceProvider>> getAllApprovedProviders(){
        return new ResponseEntity<List<ServiceProvider>>(this.providerService.getAllApprovedProviders(), HttpStatus.OK);
    }

    @PostMapping("/add_service/{providerId}")
    public ResponseEntity<ServicesDto> addServicesToProvider(@ModelAttribute AddServiceDto service, @PathVariable Integer providerId
                                                            , @RequestParam(value = "serviceImage", required = false) MultipartFile file){
        if(file != null){
            service.setServiceImage(file);
        }
        ServicesDto services = this.servicesService.addService(service, providerId);
        System.out.println(services);
        return new ResponseEntity<ServicesDto>(services, HttpStatus.OK);
    }

    //sevices controller
    @GetMapping("/{providerId}/services")
    public ResponseEntity<List<ServicesDto>> getAllServicesByProvider(@PathVariable Integer providerId) {
        List<ServicesDto> services = servicesService.getAllServicesByProvider(providerId);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServicesDto>> getAllServices() {
        List<ServicesDto> services = servicesService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/services/category/{category}")
    public ResponseEntity<List<ServicesDto>> getAllServicesByCategory(@PathVariable String category) {
        List<ServicesDto> services = servicesService.getAllServicesByCategory(category);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/services/search")
    public ResponseEntity<List<ServicesDto>> getSearchedServices(@RequestParam String search) {
        List<ServicesDto> services = servicesService.getSearchedServices(search);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/services/{serviceId}")
    public ResponseEntity<Services> getServiceById(@PathVariable Integer serviceId) {
        Services services = servicesService.getServiceById(serviceId);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @PutMapping("/services/{serviceId}")
    public ResponseEntity<Services> updateService(@Valid @PathVariable Integer serviceId, AddServiceDto services,
                                                  @RequestParam(value = "serviceImage", required = false) MultipartFile file) {
        if(file != null){
            services.setServiceImage(file);
        }
        Services updatedService = servicesService.updateService(serviceId, services);
        return new ResponseEntity<>(updatedService, HttpStatus.OK);
    }

    @DeleteMapping("/services/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable Integer serviceId) {
        servicesService.deleteService(serviceId);
        return new ResponseEntity<>("Service deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/suggested-providers")
    public ResponseEntity<List<ServiceProvider>> getSuggestedProviders(@RequestParam Integer providerId) {
        List<Integer> suggestedProviderIds = providerService.getSuggestedProviderIds(providerId);
        List<ServiceProvider> suggestedProviders = providerService.getProvidersByIds(suggestedProviderIds);
        return new ResponseEntity<>(suggestedProviders, HttpStatus.OK);
    }




}
