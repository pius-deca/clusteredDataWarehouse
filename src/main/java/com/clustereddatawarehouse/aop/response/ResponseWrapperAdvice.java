package com.clustereddatawarehouse.aop.response;

import com.clustereddatawarehouse.annotation.ResponseWrapper;
import com.clustereddatawarehouse.aop.error.ValidationException;
import com.clustereddatawarehouse.service.validator.ValidatorError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    private final Logger log = LoggerFactory.getLogger(ResponseWrapperAdvice.class);
    private static final String ERROR_MESSAGE = "An error has occurred";

    private final MessageSource messageSource;

    public ResponseWrapperAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.getContainingClass().isAnnotationPresent(ResponseWrapper.class) ||
            methodParameter.hasMethodAnnotation(ResponseWrapper.class);
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        log.info("START: writing generic response to object..");
        if(object instanceof InputStream || object instanceof InputStreamSource) return object;
        String message = "";
        ResponseWrapper methodWrapper = AnnotationUtils.findAnnotation(methodParameter.getAnnotatedElement(), ResponseWrapper.class);
        ResponseWrapper classWrapper = AnnotationUtils.findAnnotation(methodParameter.getContainingClass(), ResponseWrapper.class);
        if (methodWrapper != null) {
            message = methodWrapper.message();
        } else if(classWrapper != null){
            message = classWrapper.message();
        }
        log.info("Payload gotten: {}", object);
        GenericResponse<Object> genericResponse = new GenericResponse<>();
        genericResponse.setStatus(GenericResponse.SUCCESS_KEY);
        genericResponse.setMessage(message);
        genericResponse.setData(object);
        if (response instanceof ServletServerHttpResponse) {
            int statusCode = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
            HttpStatus status = HttpStatus.valueOf(statusCode);
            if(status.is4xxClientError() || status.is5xxServerError()) {
                genericResponse.setStatus(GenericResponse.FAILED_KEY);
                genericResponse.setMessage(ERROR_MESSAGE);
            }
        }
        log.info("END: writing generic response to object..");
        return genericResponse;
    }

    @ExceptionHandler
    public ResponseEntity<GenericResponse<ErrorResponse>> handleAllException(Exception ex,  Locale locale) {
        log.info("START: writing generic error response to object..");
        GenericResponse<ErrorResponse> response = new GenericResponse<>();
        response.setStatus(GenericResponse.FAILED_KEY);
        response.setMessage(String.format("%s at entity", ERROR_MESSAGE));
        ErrorResponse errorResponse = problemDelegator(ex, locale);
        response.setData(errorResponse);
        log.info("END: writing generic error response to object..");
        return ResponseEntity.status(errorResponse.getStatusCode() != null ? HttpStatus.valueOf(errorResponse.getStatusCode()) : HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(response);
    }

    private ErrorResponse problemDelegator(Exception exception, Locale locale) {
        ErrorResponse response = new ErrorResponse();
        if (exception instanceof ValidationException) {
            ValidationException ex = (ValidationException)exception;
            Function<ValidatorError, String> localeMapper = validatorError ->
                    messageSource.getMessage(validatorError.getErrorKey(), validatorError.getArguments(), locale);
            List<String> messages = CollectionUtils.isEmpty(ex.getValidatorErrors())
                    ? List.of()
                    : ex.getValidatorErrors().stream().filter(Objects::nonNull).map(localeMapper).collect(Collectors.toList());
            response.setStatusCode((short) HttpStatus.UNPROCESSABLE_ENTITY.value());
            response.setMessage(messages);
        } else if(exception instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) exception;
            response.setStatusCode((short)responseStatusException.getRawStatusCode());
            response.setMessage(responseStatusException.getMessage());

        } else {
            response.setStatusCode(((short) HttpStatus.INTERNAL_SERVER_ERROR.value()));
            response.setMessage(exception.getMessage());
        }
        return response;
    }

}
