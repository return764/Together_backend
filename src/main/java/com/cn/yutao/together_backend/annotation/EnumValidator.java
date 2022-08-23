package com.cn.yutao.together_backend.annotation;

import lombok.extern.log4j.Log4j2;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = EnumValidator.Validator.class)
public @interface EnumValidator {

    String message() default "input value is not in enums.";

    Class<?> value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Log4j2
    class Validator implements ConstraintValidator<EnumValidator, Object> {

        List<Object> enumValues = new ArrayList<>();

        @Override
        public void initialize(EnumValidator anno) {
            final Class<?> clz = anno.value();
            if (!clz.isEnum()) {
                throw new RuntimeException(clz.getName() + "is not correctly enum class.");
            }
            final Object[] enumConstants = clz.getEnumConstants();
            try {
                final var getValue = clz.getMethod("value");
                for (Object enumConstant : enumConstants) {
                    final var result = getValue.invoke(enumConstant);
                    enumValues.add(result);
                }
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            return Objects.nonNull(value) && enumValues.contains(value);
        }
    }
}
