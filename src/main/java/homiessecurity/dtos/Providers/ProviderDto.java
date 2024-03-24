package homiessecurity.dtos.Providers;

import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.Services;
import homiessecurity.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderDto {

    private Integer providerId;
    private String providerName;
    private String email;
    private String description;
    private String phoneNumber;
    private String address;
    private String status;

    private List<Services> allServices;
    private ServiceCategory categories;

}
