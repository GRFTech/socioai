package spring.gr.socioai.util.annotations.notzero;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotZeroValidator.class)
public @interface NotZero {
    String message() default "O valor não pode ser zero";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
