package com.dpmg.patrimonio.infra;

import com.dpmg.patrimonio.exceptions.*;
import com.dpmg.patrimonio.models.dtos.shared.RestErrorDTO;
import com.dpmg.patrimonio.utils.Messages;
import com.dpmg.patrimonio.utils.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<RestErrorDTO> runtimeExceptionHandler(RuntimeException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), Messages.GENERIC_ERROR);
        return ResponseEntity.internalServerError().body(threatResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    private ResponseEntity<RestErrorDTO> noResourceFoundExceptionHandler(NoResourceFoundException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.NOT_FOUND.value(), Messages.ROUTE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<RestErrorDTO> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.METHOD_NOT_ALLOWED.value(), Messages.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(threatResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorDTO> validationExceptionHandler(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ObjectError> objectErrors = exception.getBindingResult().getGlobalErrors();
        String firstErrorMessage = ValidationUtils.getFirstErrorMessage(objectErrors, fieldErrors);

        return ResponseEntity.badRequest().body(new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), firstErrorMessage));
    }

    @ExceptionHandler(FailedImportException.class)
    private ResponseEntity<RestErrorDTO> failedImportExceptionHandler(FailedImportException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.CONFLICT.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(threatResponse);
    }

    @ExceptionHandler(InvalidImportFileException.class)
    private ResponseEntity<RestErrorDTO> invalidImportFileExceptionHandler(InvalidImportFileException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(CanNotUpdateItemIfInventoryIsNotOpenException.class)
    private ResponseEntity<RestErrorDTO> canNotUpdateItemIfInventoryIsNotOpenExceptionHandler(CanNotUpdateItemIfInventoryIsNotOpenException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(CanNotUpdateFinishedInventoryException.class)
    private ResponseEntity<RestErrorDTO> canNotUpdateFinishedInventoryExceptionHandler(CanNotUpdateFinishedInventoryException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(CanNotImportInventoryException.class)
    private ResponseEntity<RestErrorDTO> canNotImportInventoryExceptionHandler(CanNotImportInventoryException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(ThisItemWasAlreadyLocatedException.class)
    private ResponseEntity<RestErrorDTO> thisItemWasAlreadyLocatedExceptionHandler(ThisItemWasAlreadyLocatedException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(InvalidOtherSituationIdException.class)
    private ResponseEntity<RestErrorDTO> invalidOtherSituationIdExceptionHandler(InvalidOtherSituationIdException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(WrongSheetNameException.class)
    private ResponseEntity<RestErrorDTO> wrongSheetNameExceptionHandler(WrongSheetNameException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(InvalidStatusException.class)
    private ResponseEntity<RestErrorDTO> invalidStatusExceptionHandler(InvalidStatusException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    private ResponseEntity<RestErrorDTO> inventoryNotFoundExceptionHandler(InventoryNotFoundException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(ImportAlreadyInProgressException.class)
    private ResponseEntity<RestErrorDTO> importAlreadyInProgressExceptionHandler(ImportAlreadyInProgressException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    private ResponseEntity<RestErrorDTO> itemNotFoundExceptionHandler(ItemNotFoundException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(NotManualRegistrationException.class)
    private ResponseEntity<RestErrorDTO> notManualRegistrationExceptionHandler(NotManualRegistrationException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }

    @ExceptionHandler(UnitNotFoundException.class)
    private ResponseEntity<RestErrorDTO> unitNotFoundExceptionHandler(UnitNotFoundException exception) {
        RestErrorDTO threatResponse = new RestErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.badRequest().body(threatResponse);
    }
}
