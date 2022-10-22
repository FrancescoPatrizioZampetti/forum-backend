package com.blackphoenixproductions.forumbackend.adapters.api;
import com.blackphoenixproductions.forumbackend.adapters.api.exception.ErrorResponse;
import com.blackphoenixproductions.forumbackend.adapters.api.dto.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandlerController {

  public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);


  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage(), ex.getHttpStatus()), ex.getHttpStatus());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(new ErrorResponse("Accesso negato.", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(AsyncRequestTimeoutException.class)
  public void handleAsyncRequestTimeoutException(Exception ex) {
    logger.error(ex.getMessage(), ex);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    logger.error(ex.getMessage(), ex);
    BindingResult result = ex.getBindingResult();
    final List<FieldError> fieldErrors = result.getFieldErrors();
    List<String> errorMessages = fieldErrors.stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
    return new ResponseEntity<ErrorResponse>(new ErrorResponse(errorMessages.toString(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(Exception ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
