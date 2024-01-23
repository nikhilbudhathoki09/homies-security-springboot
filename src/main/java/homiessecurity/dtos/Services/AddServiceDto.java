package homiessecurity.dtos.Services;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddServiceDto {

    private String serviceName;

    private String description;

    private double perHourRate;

    private Integer categoryId;
    
}
