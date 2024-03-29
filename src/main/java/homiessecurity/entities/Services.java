package homiessecurity.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class Services {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    private Integer id;

    private String description;

    private double perHourRate;

    private String  serviceName;

    private String serviceImage;
    
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonBackReference(value = "category_services")
    private ServiceCategory category;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @JsonBackReference(value = "provider-service")
    private ServiceProvider provider;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @JsonBackReference(value = "service-appointment")
    private List<Appointment> appointments;


}
