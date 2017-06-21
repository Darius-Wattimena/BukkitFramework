package nl.antimeta.bukkit.framework.database.annotation;

import nl.antimeta.bukkit.framework.database.model.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
    String name() default "";

    boolean primary() default false;

    boolean nullable() default false;

    FieldType fieldType() default FieldType.Unknown;

    int size() default 0;

    int digitSize() default 0;
}
