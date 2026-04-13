package com.saveit.bff.notes.validation;

import com.saveit.bff.notes.validation.validator.TagsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TagsValidator.class)
@Documented
public @interface ValidTags {

    String message() default "Each tag must have non-empty name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}