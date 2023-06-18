package com.drps.ams.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.Utils;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiResponseEntity> exception(InvalidEmailException exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_INVALID_EMAIL_EXCEPTION, exception.getMessage());      
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@ExceptionHandler(NoRecordFoundException.class)
    public ResponseEntity<ApiResponseEntity> exception(NoRecordFoundException exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION, exception.getMessage());      
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@ExceptionHandler(RecordIdNotFoundException.class)
    public ResponseEntity<ApiResponseEntity> exception(RecordIdNotFoundException exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_NO_RECORD_FOUND_EXCEPTION, exception.getMessage());      
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<ApiResponseEntity> exception(DuplicateRecordException exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_DUPLICATE_RECORD_EXCEPTION, exception.getMessage());      
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<ApiResponseEntity> exception(UserDisabledException exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_USER_DISABLED_EXCEPTION, exception.getMessage());      
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponseEntity> exception(InvalidCredentialsException exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_INVALID_CREDENTIALS_EXCEPTION, exception.getMessage());      
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@ExceptionHandler(UserContextNotFoundException.class)
    public ResponseEntity<ApiResponseEntity> exception(UserContextNotFoundException exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_USER_CONTEXT_NOT_FOUND_EXCEPTION, exception.getMessage());      
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<ApiResponseEntity> exception(UnsupportedMediaTypeException exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_UNSUPPORTED_MEDIA_TYPE, exception.getMessage());      
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseEntity> exception(Exception exception, WebRequest webRequest) {
    	ApiResponseEntity response = new ApiResponseEntity(ApiConstants.RESP_STATUS_EXCEPTION, exception.getCause().getCause().getMessage());      
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
