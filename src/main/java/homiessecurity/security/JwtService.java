package homiessecurity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

//    private static final String SECRECT_KEY = "cba6ccd71b7c6679a01d912e1fa7ba3e9af7c4349902cca3da5deb374f2c0078";
    @Value("${application.homies.jwt.secret-key}")
    private String secretKey;

    @Value("${application.homies.jwt.expiration}")
    private long jwtExpiration ;

    @Value("${application.homies.jwt.refresh-token.expiration}")
    private long refreshExpiration;



    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Method for extracting single claims we pass
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() { //returns the encoded value in form of key using hmacShake
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generateToken(Map<String,Object> extraClaims,
                                UserDetails userDetails) {
//        return Jwts
//                .builder()
//                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //24 hours
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    //Now creating token with expiration time

    public String buildToken(Map<String,Object> extraClaims,
                             UserDetails userDetails,
                             long expiration){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);

    }

    //For refresh Tokens
    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails,refreshExpiration);
    }

    public Boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

}
