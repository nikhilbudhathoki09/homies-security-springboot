package homiessecurity.entities;

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


    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Locations location;
    
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Services> allServices = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "provider_categories", joinColumns = @JoinColumn(name= "provider_id"),
               inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<ServiceCategory> categories = new ArrayList<>(3);  //making the size of the category to 3 so the provider can only choose 3 categories

    @OneToMany(mappedBy = "provider")
    @JsonManagedReference
    private List<Appointment> appointments = new ArrayList<>();


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
