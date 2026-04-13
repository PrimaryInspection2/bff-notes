package com.saveit.bff.notes.validation.validator;

import com.saveit.bff.notes.validation.ValidTags;
import com.saveit.bff.notes.web.dto.TagDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class TagsValidator implements ConstraintValidator<ValidTags, Set<TagDto>> {

    @Override
    public boolean isValid(Set<TagDto> tags, ConstraintValidatorContext context) {

        if (tags == null || tags.isEmpty()) {
            return true;
        }

        for (TagDto tag : tags) {
            if (tag == null || tag.name() == null || tag.name().isBlank()) {

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Tag name must not be blank")
                        .addConstraintViolation();

                return false;
            }
        }
        return true;
    }
}