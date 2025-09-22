package com.example.text.demoOnLine.参数校验;

import lombok.extern.log4j.Log4j2;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

/**
 * CustomValidImpl
 *
 * @author yuez
 * @since 2024/1/12
 */
@Log4j2
public class CustomValidImpl implements ConstraintValidator<CustomValid, String> {
    private static final String REGEX = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$";
    Pattern pattern = Pattern.compile(REGEX);
    @Override
    public void initialize(CustomValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(s).matches();
    }
}
