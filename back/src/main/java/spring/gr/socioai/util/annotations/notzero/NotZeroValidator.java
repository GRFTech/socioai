package spring.gr.socioai.util.annotations.notzero;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotZeroValidator implements ConstraintValidator<NotZero, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value != 0.0;
    }
}
