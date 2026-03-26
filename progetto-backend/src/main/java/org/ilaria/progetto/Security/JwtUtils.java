package org.ilaria.progetto.Security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs; // tempo di durata del token

    private SecretKey getSingingKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /* dice qual'é l'algoritmo di codifica ma non la chiave - BASE64 codifica un oggetto/file in stringa */

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSingingKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    /*  l'algoritmo di cifratura all'interno serve per decodificare , per questo non va messa la password all'interno
    perché + facilmente decodificabile, l'essenziale è fare in modo che il token non venga rubato */

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSingingKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSingingKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        }catch (JwtException e ){
            e.printStackTrace();
        }
        return false;
    }

}
