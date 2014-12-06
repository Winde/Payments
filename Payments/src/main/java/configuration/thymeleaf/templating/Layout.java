package configuration.thymeleaf.templating;

import java.lang.annotation.*;
 
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Layout {
    String value() default "";
}