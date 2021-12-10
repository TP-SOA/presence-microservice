package fr.iss.soa.presencemicroservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PresenceDetectorNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(PresenceDetectorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String presenceDetectorNotFoundHandler(PresenceDetectorNotFoundException exception) {
        return exception.getMessage();
    }
}
