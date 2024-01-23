package homiessecurity.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="providers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer providerId;

    @Column(name = "providerName")
    private String providerName;
    
    private String email;

    @Column(name = "description")
    private String description;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "registrationDocument" )
    @Nullable
    private String registrationDocument;


    @Column(name = "address")
    private String address;

    @Column(name = "password")
    private String password;

    @Column(name = "providerImage")
    private String providerImage;

    @Column(name = "experienceCertificate")
    private String experienceDocument;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProviderStatus status;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;


    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Locations location;
    
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Services> allServices = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "provider_categories", joinColumns = @JoinColumn(name= "provider_id"),
               inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<ServiceCategory> categories = new ArrayList<>(3);  //making the size of the category to 3 so the provider can only choose 3 categories






    

}
