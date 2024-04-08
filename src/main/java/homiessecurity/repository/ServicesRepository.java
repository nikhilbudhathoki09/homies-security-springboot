package homiessecurity.repository;

import homiessecurity.entities.Services;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Integer>{

    Optional<Services> findByServiceName(String serviceName);

    Optional<List<Services>> findByCategoryTitle(String categoryName);

    @Query("SELECT s FROM Services s WHERE s.provider.providerId = ?1")
    Optional<List<Services>> findByProviderId(Integer providerId);

    @Query(value = "SELECT s FROM Services s WHERE LOWER(s.serviceName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Services> findSearchedServices(@Param("search") String search);

    @Query("SELECT s FROM Services s JOIN s.provider p JOIN p.location l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :locationName, '%'))")
    List<Services> findServicesByProviderLocation(@Param("locationName") String locationName);

    @Query("SELECT s FROM Services s JOIN s.category c JOIN s.provider p JOIN p.location l WHERE c.id = :categoryId AND l.id = :locationId")
    List<Services> findByCategoryIdAndLocationId(@Param("categoryId") Integer categoryId, @Param("locationId") Integer locationId);

    @Query("SELECT s FROM Services s JOIN s.provider p JOIN p.location l WHERE l.id = :locationId")
    List<Services> findServicesByProviderLocationId(@Param("locationId") Integer locationId);


    List<Services> findByPerHourRateBetween(Double minPrice, Double maxPrice);


    @Query("SELECT s FROM Services s WHERE s.perHourRate BETWEEN :min AND :max AND s.category.id = :categoryId")
    List<Services> findByPerHourRateBetweenAndCategory(@Param("min") Double min, @Param("max") Double max, @Param("categoryId") Integer categoryId);














}
