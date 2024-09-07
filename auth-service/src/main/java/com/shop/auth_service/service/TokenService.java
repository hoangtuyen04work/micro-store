package com.shop.auth_service.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.shop.auth_service.dto.TokenRequest;
import com.shop.auth_service.dto.UserResponse;
import com.shop.auth_service.entity.Role;
import com.shop.auth_service.entity.Token;
import com.shop.auth_service.exception.AppException;
import com.shop.auth_service.exception.ErrorCode;
import com.shop.auth_service.repository.TokenRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.StringJoiner;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Slf4j
public class TokenService {
    @Value("${jwt.signerKey}")
    @NonFinal
    String SIGNER_KEY;
    TokenRepo tokenRepo;

    public Token getTokenByRefreshToken(String refreshToken){
        return tokenRepo.findByRefreshToken(refreshToken);
    }
    public boolean isRefreshTokenExisted(String refreshToken){
        return tokenRepo.existByRefreshToken(refreshToken);
    }
    public boolean isRefreshTokenValid(String refreshToken){
        if(!isRefreshTokenExisted(refreshToken)) return false;
        Token token = getTokenByRefreshToken(refreshToken);
        if(token.getExpiry_time().before(new Date())) return false;
        return tokenRepo.findByRefreshToken(refreshToken) != null;
    }
    public void save(Token token) {
        tokenRepo.save(token);
    }
    public void deleteToken(Token token){
        tokenRepo.delete(token);
    }
    public void deleteToken(String token){
        tokenRepo.deleteByToken(token);
    }
    public void deleteToken(TokenRequest request){
        deleteToken(request.getToken());
    }
    public String getUserIdByToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public boolean isTokenValid(String token) {
        return tokenRepo.existsByToken(token);
    }

    public boolean checkToken(String token) throws AppException {
        verifyToken(token);
        return true;
    }

    public SignedJWT verifyToken(String token) throws  AppException {
        try{
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime =   signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean verified = signedJWT.verify(verifier);
            if(!verified || expiryTime.before(new Date())){
                throw new AppException(ErrorCode.NOT_AUTHENTICATED);
            }
            return signedJWT;
        } catch (Exception e){
            throw  new AppException(ErrorCode.NOT_AUTHENTICATED);
        }
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
    public Token generateToken(UserResponse user) throws JOSEException {
        return Token.builder()
                .token(generateTokenString(user))
                .expiry_time(Date.from(Instant.now().plus(15*24*60*60, ChronoUnit.MILLIS)))
                .refreshToken(generateRefreshToken())
                .refresh_expiry_time(Date.from(Instant.now().plus(30*24*60*60, ChronoUnit.MILLIS)))
                .build();
    }
    public String generateTokenString(UserResponse user) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        String roleScope = buildRole(user.getRoles());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("HTShop")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(15*24*60*60, ChronoUnit.MILLIS))) // Adjust as needed
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", roleScope)
                .build();
        JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(jwtClaimsSet.toJSONObject()));
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    }

    private  String buildRole(Set<Role> roles){
        StringJoiner joiner = new StringJoiner(" ");
        for(Role role : roles){
            joiner.add(role.getRoleName());
        }
        return joiner.toString();
    }

}
