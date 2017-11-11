package net.pechorina.kontempl.rest;

import net.pechorina.kontempl.data.KtError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {
    private final Logger log = LoggerFactory.getLogger(ExceptionTranslator.class);

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public KtError processAccessDeniedException(AccessDeniedException e) {
        log.warn("Access denied", e);
        return KtError.newBuilder()
                .withStatus(401)
                .withException(e.getMessage())
                .withError("AccessDenied")
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public KtError processHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException", e);
        return KtError.newBuilder()
                .withStatus(400)
                .withException(e.getMessage())
                .withError("httpMessageNotReadable")
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public KtError unhandledException(RuntimeException e) {
        log.error("Unhandled Exception", e);
        return KtError.newBuilder()
                .withStatus(500)
                .withException(e.getMessage())
                .withError("internalServerError")
                .build();
    }
}
