package homiessecurity.repository;

import homiessecurity.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

    @Query("Select t from RefreshToken t inner join User u on t.user.userId = u.userId where u.userId = :userId and (t.expired = false or t.revoked = false)")
    List<RefreshToken> findAllValidTokensByUserId(@Param("userId") Integer userId);

    Optional<RefreshToken> findByToken(String token);
}
