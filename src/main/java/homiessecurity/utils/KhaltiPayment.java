package homiessecurity.utils;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import homiessecurity.dtos.khalti.KhaltiInitiationRequest;
import homiessecurity.dtos.khalti.KhaltiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class KhaltiPayment {

    @Value("${khalti.api.url}")
    private String khaltiBaseUrl;

    @Value("${khalti.api.key}")
    private String khaltiSecretKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KhaltiPayment(
            RestTemplate restTemplate,
            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public KhaltiResponseDTO callKhalti(KhaltiInitiationRequest request) throws JsonProcessingException {

        // Convert the request object to JSON
        String formDataJson = objectMapper.writeValueAsString(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "key " + khaltiSecretKey);
        HttpEntity<String> entity = new HttpEntity<>(formDataJson, headers);

        ResponseEntity<KhaltiResponseDTO> responseEntity = restTemplate.exchange(
                khaltiBaseUrl + "/epayment/initiate/",
                HttpMethod.POST,
                entity,
                KhaltiResponseDTO.class);

        return responseEntity.getBody();
    }

}
