package com.ettarak.todo.exception;

import com.ettarak.todo.model.HttpResponse;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.ettarak.todo.utils.Utils.dateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {

    // Handling Invalid Method Arguments
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(exception.getMessage());

        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();
        String fieldMessage = fieldErrorList.stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason("Invalid fields: " + fieldMessage)
                        .developerMessage(exception.getMessage())
                        .status((HttpStatus) status)
                        .statusCode(status.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), status);
    }

    // Handling Internal Exception
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(exception.getMessage())
                        .developerMessage(exception.getMessage())
                        .status((HttpStatus) status)
                        .statusCode(status.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), status);
    }

    // Handling Illegal State Exception
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<HttpResponse<?>> illegalStateException(IllegalStateException exception) {
        return  createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<HttpResponse<?>> noteNotFoundException(NoteNotFoundException exception) {
        return  createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse<?>> noResultException(NoResultException exception) {
        return  createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }
    @ExceptionHandler(ServletException.class)
    public ResponseEntity<HttpResponse<?>> servletException(ServletException exception) {
        return  createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse<?>> exception(Exception exception) {
        return  createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    // function
    private ResponseEntity<HttpResponse<?>> createHttpErrorResponse(HttpStatus httpStatus, String reason, Exception exception) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(reason)
                        .developerMessage(exception.getMessage())
                        .status((HttpStatus) httpStatus)
                        .statusCode(httpStatus.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), httpStatus);
    }

}
