package com.clustereddatawarehouse.service;

import com.clustereddatawarehouse.service.dto.DealDTO;

import java.util.List;

public interface DealService {
    DealDTO saveDeal(DealDTO dealDTO);

    DealDTO getDealById(Long id);

    List<DealDTO> getAllDeals();
}
