package api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    long timeout() default -1;

    Class<?> expected() default ExpectedDefaultClass.class;

    String[] dependsOnMethod() default {};
}
