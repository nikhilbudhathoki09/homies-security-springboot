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
    @JsonBackReference
    private ServiceCategory category;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @JsonBackReference
    private ServiceProvider provider;

    @OneToMany(mappedBy = "service")
    @JsonBackReference
    private List<Appointment> appointments;


}
