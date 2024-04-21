package homiessecurity.dtos.Providers;

import homiessecurity.entities.Locations;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.Services;
import homiessecurity.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.stream.Location;
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
    private String providerImage;
    private ServiceCategory category;
    private List<Services> allServices;
    private double yearOfExperience;
    private double minServicePrice;
    private double maxServicePrice;
    private Locations location;

}
