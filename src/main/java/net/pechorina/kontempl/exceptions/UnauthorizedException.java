package net.pechorina.kontempl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Unauthorized")
public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

}
