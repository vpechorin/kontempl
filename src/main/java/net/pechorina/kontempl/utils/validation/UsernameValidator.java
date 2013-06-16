package net.pechorina.kontempl.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.pechorina.kontempl.service.UserService;
import net.pechorina.kontempl.utils.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class UsernameValidator implements ConstraintValidator<Username, Object> {
	private static final Logger logger = Logger.getLogger(UsernameValidator.class);

	@Autowired
	private UserService userService;
	
	@Override
	public void initialize(Username constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String username = null;
		
		if (value != null) {
			username = (String) value;
			username = StringUtils.superTrim(username);
			username = username.toLowerCase();
		}
		
		boolean isValid = false;
		
		if (username == null) {
			logger.debug("username validation failed: null");
			isValid = false;
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{NotBlank.reg.email}").addConstraintViolation();
		}
		else if (username.length() < 5) {
			logger.debug("username validation failed: size is " + username.length() + " [" + username + "]");
			isValid = false;
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{Size.reg.email}").addConstraintViolation();
		}
		else {
			try {
				boolean available = userService.checkIfEmailAvailable(username);
				if (available) {
					isValid = true;
				}
				else {
					isValid = false;
				}
				if (!isValid) {
					logger.debug("username validation failed: email in use" + " [" + username + "]");
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{InUse.reg.email}").addConstraintViolation();
				}
			}
			catch (Exception e) {
				logger.error("Exception - username validation failed: " + "[" + username + "]");
				e.printStackTrace();
			}
		}

		return isValid;
	}

}
