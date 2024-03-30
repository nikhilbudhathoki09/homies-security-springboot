package homiessecurity.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name ="providers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceProvider implements UserDetails {

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
    private Status status;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "subscription_token")
    private String subscriptionToken;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @JsonBackReference(value = "location_providers")
    private Locations location;

    private double yearOfExperience;

    @Column(name = "min_service_price")
    private double minServicePrice;

    @Column(name = "max_service_price")
    private double maxServicePrice;

    @Column(name = "average_rating")
    private Double averageRating;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "provider-service")
    private List<Services> allServices = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonBackReference(value = "category_providers")
    private ServiceCategory category;

    @OneToMany(mappedBy = "provider",cascade = CascadeType.ALL)
    @JsonManagedReference(value = "provider-appointment")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "provider",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "provider-rating")
    private List<Rating> ratings = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities= new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PROVIDER"));
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
