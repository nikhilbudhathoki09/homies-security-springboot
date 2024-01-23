package homiessecurity.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "services")
public class Services {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    private Integer id;

    private String description;

    private double perHourRate;

    private String  serviceName;
    
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ServiceCategory category;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private ServiceProvider provider;

    
}
