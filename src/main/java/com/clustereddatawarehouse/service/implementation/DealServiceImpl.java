package com.clustereddatawarehouse.service.implementation;

import com.clustereddatawarehouse.aop.error.ValidationException;
import com.clustereddatawarehouse.model.Deal;
import com.clustereddatawarehouse.repository.DealRepository;
import com.clustereddatawarehouse.service.DealService;
import com.clustereddatawarehouse.service.dto.DealDTO;
import com.clustereddatawarehouse.service.mapper.DealMapper;
import com.clustereddatawarehouse.service.validator.DealValidator;
import com.clustereddatawarehouse.service.validator.ValidatorError;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private DealMapper dealMapper;
    private final DealValidator dealValidator;

    public DealServiceImpl(DealRepository dealRepository, DealMapper dealMapper, DealValidator dealValidator) {
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
        this.dealValidator = dealValidator;
    }

    @Override
    public DealDTO saveDeal(DealDTO dealDTO) {
        List<ValidatorError> validationErrors = dealValidator.validateDeal(dealDTO);
        if(!CollectionUtils.isEmpty(validationErrors)) throw new ValidationException(validationErrors);
        Deal deal = dealMapper.toEntity(dealDTO);
        deal = dealRepository.save(deal);
        return dealMapper.toDto(deal);
    }

    @Override
    public DealDTO getDealById(Long id) {
        List<ValidatorError> validationErrors = dealValidator.validateDealById(id);
        if(!CollectionUtils.isEmpty(validationErrors)) throw new ValidationException(validationErrors);
        Optional<DealDTO> optionalFxDeal = dealRepository.findById(id).map(dealMapper::toDto);
        return optionalFxDeal.orElseThrow(() -> new ValidationException(List.of()));
    }

    @Override
    public List<DealDTO> getAllDeals() {
        List<Deal> fxDeals = dealRepository.findAll();
        return fxDeals.stream().map(dealMapper::toDto).collect(Collectors.toList());
    }
}
