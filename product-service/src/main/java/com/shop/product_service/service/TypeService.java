package com.shop.product_service.service;

import com.shop.product_service.dto.TypeRequest;
import com.shop.product_service.dto.TypeResponse;
import com.shop.product_service.entity.Type;
import com.shop.product_service.exception.AppException;
import com.shop.product_service.exception.ErrorCode;
import com.shop.product_service.repository.TypeRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class TypeService {
    TypeRepo typeRepo;
    public boolean isExistByTypeName(String type){
        return typeRepo.existsByType(type);
    }
    public TypeResponse addType(TypeRequest typeRequest) {
        return toTypeResponse(typeRepo.save(toType(typeRequest)));
    }
    public List<TypeResponse> getAllTypes() {
        List<Type> types = typeRepo.findAll();
        List<TypeResponse> typeResponses = new ArrayList<>();
        for (Type type : types) {
            typeResponses.add(toTypeResponse(type));
        }
        return typeResponses;
    }
    public TypeResponse getTypeResponseById(String id) throws AppException {
        return toTypeResponse(typeRepo.findById(id).orElseThrow(()-> new AppException(ErrorCode.INVALID_INPUT)));
    }
    public Type getTypeById(String id){
        return typeRepo.findById(id).get();
    }
    public List<Type> getTypes(List<TypeRequest> typeRequests) throws AppException {
        List<Type> types = new ArrayList<>();
        for (TypeRequest typeRequest : typeRequests) {
            types.add(getTypeById(typeRequest.getId()));
        }
        return types;
    }
    public List<TypeResponse> getTypeResponsesFromRequests(List<TypeRequest> typeRequests) throws AppException {
        List<Type> types = getTypes(typeRequests);
        List<TypeResponse> typeResponses = new ArrayList<>();
        for (Type type : types) {
            typeResponses.add(toTypeResponse(type));
        }
        return typeResponses;
    }
    public List<TypeResponse> getTypeResponses(List<Type> types) throws AppException {
        List<TypeResponse> typeResponses = new ArrayList<>();
        for (Type type : types) {
            typeResponses.add(toTypeResponse(type));
        }
        return typeResponses;
    }

    public boolean removeTypeById(String id) throws AppException {
        typeRepo.deleteById(id);
        return true;
    }
    public boolean removeTypeByName(String typeName) throws AppException {
        typeRepo.deleteByType(typeName);
        return true;
    }
    public TypeResponse updateType(TypeRequest typeRequest) throws AppException {
        return toTypeResponse(typeRepo.save(toType(typeRequest)));
    }
    public Type toType(TypeRequest typeRequest) {
        return Type.builder()
                .type(typeRequest.getType())
                .build();
    }
    public TypeResponse toTypeResponse(Type type) {
        return TypeResponse.builder()
                .type(type.getType())
                .id(type.getId())
                .build();
    }
}
