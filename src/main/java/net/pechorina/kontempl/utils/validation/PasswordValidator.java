package net.pechorina.kontempl.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.pechorina.kontempl.utils.StringUtils;

import org.apache.log4j.Logger;

public class PasswordValidator implements ConstraintValidator<Password, Object> {
	private static final Logger logger = Logger.getLogger(PasswordValidator.class);
	
	private static final int MIN_PASSWORD_LENGTH = 5;
	
	@Override
	public void initialize(Password constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String password = null;
		boolean isValid = false;
		
		if (value != null) {
			password = (String) value;
			String trimmedPassword = StringUtils.superTrim(password);
			boolean trimmed = true;
			if (trimmedPassword.equals(password)) {
				trimmed = false;
			}
			
			if (trimmedPassword.length() < MIN_PASSWORD_LENGTH) {
				isValid = false;
				if (trimmed) {
					logger.debug("username validation failed: trimmed password shorter than " + MIN_PASSWORD_LENGTH);
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{NoSpacesSize.reg.password}").addConstraintViolation();
				}
				else {
					logger.debug("username validation failed: password shorter than " + MIN_PASSWORD_LENGTH);
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{Size.reg.password}").addConstraintViolation();
				}
			}
			else {
				isValid = true;
			}
		}
		else {
			logger.debug("password validation failed: null");
			isValid = false;
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{NotBlank.reg.password}").addConstraintViolation();
		}

		return isValid;
	}

}
