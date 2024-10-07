package riwi.com.springsecurityjwt.Security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Slf4j
@Component
public class JWTUtils {

    @Value("${jwt.secret.key}")
    private String secretKey; // llave secreta

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    // Generar token de acceso
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ Long.parseLong(timeExpiration) ))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //obtener firma del token
    public Key getSignatureKey(){
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //validar el token de acceso
    public boolean isTokenValid(String token) {
        try{
         Jwts.parser()
                 .setSigningKey(getSignatureKey())
                 .build()
                 .parseClaimsJws(token)
                 .getBody();

         return true;

        }catch(Exception e){
            log.error("Token invalido, error: ".concat(e.getMessage())); // imprime el mensaje de  error en la consola
            return false;
        }

    }



    //Obtener todos los claims del token
    public Claims extractAllClaims(String token){

        return Jwts.parser()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }


    // Obtener el username del token
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    // Obtener  un solo claim
    public <T> T getClaim(String token, Function<Claims,T> claimsTFunction){

        Claims claims=extractAllClaims(token);

        return claimsTFunction.apply(claims);
    }




}


