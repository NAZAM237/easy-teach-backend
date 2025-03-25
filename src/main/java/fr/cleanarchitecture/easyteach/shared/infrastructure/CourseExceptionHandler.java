package fr.cleanarchitecture.easyteach.shared.infrastructure;

import fr.cleanarchitecture.easyteach.shared.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CourseExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleIBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
