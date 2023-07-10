package com.clustereddatawarehouse.service.validator;

import com.clustereddatawarehouse.model.Deal;
import com.clustereddatawarehouse.repository.DealRepository;
import com.clustereddatawarehouse.service.dto.DealDTO;
import org.springframework.util.StringUtils;

import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DealValidator {
    private final DealRepository dealRepository;

    public DealValidator(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    public List<ValidatorError> validateDeal(DealDTO dealDTO) {
        ValidationBuilder validation = new ValidationBuilder();
        if(dealDTO == null) {
            validation.addError("error.deal.null");
        } else if (!StringUtils.hasText(dealDTO.getFromCurrency()) || isISOCurrencyCodeNotValid(dealDTO.getFromCurrency())){
            validation.addError("error.validation.currency.from.not.valid", dealDTO.getFromCurrency());
        } if (!StringUtils.hasText(dealDTO.getToCurrency()) || isISOCurrencyCodeNotValid(dealDTO.getToCurrency())) {
            validation.addError("error.validation.currency.to.not.valid", dealDTO.getToCurrency());
        } if (Objects.nonNull(dealDTO.getFromCurrency()) &&  Objects.nonNull(dealDTO.getToCurrency()) && dealDTO.getFromCurrency().equals(dealDTO.getToCurrency())) {
            validation.addError("error.validation.currency.same", dealDTO.getFromCurrency(), dealDTO.getToCurrency());
        } if (dealDTO.getAmount() == null) {
            validation.addError("error.validation.amount.not.valid");
        }
        return validation.build();
    }

    private boolean isISOCurrencyCodeNotValid(String currencyCode) {
        return Currency.getAvailableCurrencies().stream().noneMatch(c -> c.getCurrencyCode().equals(currencyCode));
    }

    public List<ValidatorError> validateDealById(Long id) {
        ValidationBuilder validation = new ValidationBuilder();
        Optional<Deal> optionalDeal = dealRepository.findById(id);
        if (optionalDeal.isEmpty()) {
            validation.addError("error.validation.deal.not.exist", id);
        }
        return validation.build();
    }
}
