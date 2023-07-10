package com.clustereddatawarehouse.service.mapper;

import com.clustereddatawarehouse.model.Deal;
import com.clustereddatawarehouse.service.dto.DealDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealMapper {

    Deal toEntity(DealDTO dto);

    DealDTO toDto(Deal entity);
}
