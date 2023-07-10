package com.clustereddatawarehouse.service.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationBuilder {

    List<ValidatorError> errors = new ArrayList<>();

    public void addError(String errorCode) {
        ValidatorError validatorError = new ValidatorError();
        validatorError.setErrorKey(errorCode);
        validatorError.setArguments(new Object[] {});
        errors.add(validatorError);
    }

    public void addError(String errorCode, Object... args) {
        ValidatorError validatorError = new ValidatorError();
        validatorError.setErrorKey(errorCode);
        validatorError.setArguments(args);
        errors.add(validatorError);
    }

    public List<ValidatorError> build() {
        return errors;
    }
}
