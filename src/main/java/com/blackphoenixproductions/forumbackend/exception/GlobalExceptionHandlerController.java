package com.blackphoenixproductions.forumbackend.exception;
import com.blackphoenixproductions.forumbackend.dto.openApi.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;


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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
