package be.intec.themarujohyperblog.config;

import be.intec.themarujohyperblog.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, User> {

    private static final Logger logger = LoggerFactory.getLogger(PasswordMatchesValidator.class);

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (user.getPassword() == null || user.getConfirmPassword() == null) {
            logger.debug("Password or ConfirmPassword is null");
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Confirm Password is required")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        boolean isValid = user.getPassword().equals(user.getConfirmPassword());
        if (!isValid) {
            logger.debug("Password: {}, ConfirmPassword: {}, isValid: {}", user.getPassword(), user.getConfirmPassword(), isValid);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
