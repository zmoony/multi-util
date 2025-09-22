package com.example.text.demoOnLine.参数校验;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义注解
 * @Constraint(validatedBy = {PhoneValidator.class})：用于指定验证器类；
 * @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})：指定@Phone注解可以作用在方法、字段、构造函数、参数以及类型上；
 * @author yuez
 */
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CustomValidImpl.class})
@Documented
public @interface CustomValid {
    String message() default "自定义格式错误";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
