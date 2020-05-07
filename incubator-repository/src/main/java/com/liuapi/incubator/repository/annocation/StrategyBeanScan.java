package com.liuapi.incubator.repository.annocation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(StrategyBeanScanRegistrar.class)
public @interface StrategyBeanScan {
    String[] scanBasePackages();

    Class[] basePackageClasses();

    String[] value() default {};
}
