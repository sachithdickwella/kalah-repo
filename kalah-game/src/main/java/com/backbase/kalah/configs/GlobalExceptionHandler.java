package com.backbase.kalah.configs;

import com.backbase.kalah.records.HttpErrorResponse;
import com.backbase.kalah.util.InvalidPitUserException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Application level exception handler. Handle every kind of exception declared under this class and
 * transform them to custom http response to understand by any client including http clients.
 *
 * Declaration of {@link ControllerAdvice} annotation make this class to be stored in Spring beans in
 * {@link org.springframework.context.ApplicationContext}.
 *
 * @author Sachith Dickwella
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle {@link InvalidPitUserException} exceptions coming from downstream services and orchestrate
     * custom {@link HttpErrorResponse} wrapped with {@link ResponseEntity}.
     *
     * @param request instance of {@link HttpServletRequest} pass by upstream function.
     * @param ex      instance that's been thrown by the services.
     * @return instance of {@link HttpErrorResponse} wrapped by {@link ResponseEntity}.
     */
    @ExceptionHandler(InvalidPitUserException.class)
    public ResponseEntity<HttpErrorResponse> handleInvalidPitUser(@NotNull HttpServletRequest request,
                                                                  @NotNull InvalidPitUserException ex) {
        final var status = HttpStatus.NOT_ACCEPTABLE;
        return ResponseEntity.status(status)
                .body(HttpErrorResponse.builder()
                        .timestamp()
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(ex.getMessage())
                        .payload(ex.getPayload())
                        .build());
    }
}
