package com.shop.auth_service.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.shop.auth_service.dto.AuthRequest;
import com.shop.auth_service.dto.UserResponse;
import com.shop.auth_service.entity.Role;
import com.shop.auth_service.entity.Token;
import com.shop.auth_service.entity.User;
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

    public Token save(Token invalidatedToken) {
        return tokenRepo.save(invalidatedToken);
    }

    public void disableToken(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(authRequest.getToken());
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Token invalidToken =
                Token.builder().id(jit).build();
        save(invalidToken);
    }

    public boolean isTokenValid(String token) {
        return tokenRepo.existsByToken(token);
    }

    public boolean checkToken(String token) throws AppException, ParseException, JOSEException {
        verifyToken(token);
        return true;
    }

    public SignedJWT verifyToken(String token) throws  AppException {
        try{
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime =   signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean verified = signedJWT.verify(verifier);
            if(!(verified && expiryTime.after(new Date()))){
                throw new AppException(ErrorCode.NOT_AUTHENTICATED);
            }
//            if (invalidatedToken.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
//                throw new AppException(ErrorCode.NOT_AUTHENTICATED);
//            }
//            else{
//
//            }
            return signedJWT;
        } catch (Exception e){
            throw  new AppException(ErrorCode.NOT_AUTHENTICATED);
        }
    }

    public String generateRefreshToken(User user) throws AppException {
        return "";
    }

    public String generateToken(UserResponse user, boolean isRefresh) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        String roleScope = buildRole(user.getRoles());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("HTShop")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // Adjust as needed
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
