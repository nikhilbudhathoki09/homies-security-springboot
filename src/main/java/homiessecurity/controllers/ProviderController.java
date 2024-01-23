package homiessecurity.controllers;

import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.service.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
import java.util.List;


@RestController
@RequestMapping("/api/v1/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService){
        this.providerService = providerService;
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

//    @GetMapping("/verified")
//    public ResponseEntity<List<ServiceProvider>> getAllVerifiedProvider(){
//        return new ResponseEntity<List<ServiceProvider>>(this.providerService.getAllVerifiedProviders(), HttpStatus.OK);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<Provider> registerServiceProvider(@RequestBody ProviderRegistrationRequestDto register){
//        ProviderRegistrationRequestDto provider = this.providerService.registerServiceProvider(register);
//        return new ResponseEntity<ProviderRegistrationRequestDto>(provider, HttpStatus.CREATED);
//    }
}
