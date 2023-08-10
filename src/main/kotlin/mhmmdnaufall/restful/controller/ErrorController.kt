package mhmmdnaufall.restful.controller

import jakarta.validation.ConstraintViolationException
import mhmmdnaufall.restful.model.WebResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class ErrorController {

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(exception: ConstraintViolationException): ResponseEntity<WebResponse<String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse(errors = exception.message))
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun responseStatusException(exception: ResponseStatusException): ResponseEntity<WebResponse<String>> {
        return ResponseEntity.status(exception.statusCode)
                .body(WebResponse(errors = exception.reason))
    }

}