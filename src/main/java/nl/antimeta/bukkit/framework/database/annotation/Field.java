package nl.antimeta.bukkit.framework.database.annotation;

import nl.antimeta.bukkit.framework.database.model.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {

    /**
     * The name of the field on the database.
     */
    String fieldName() default "";

    /**
     * Set this to true when the target field is the primary key.
     */
    boolean primary() default false;

    /**
     * Set this to true if the database value can be null.
     */
    boolean nullable() default false;

    /**
     * The type of the given field.
     */
    FieldType fieldType() default FieldType.Unknown;

    /**
     * Size of this field.
     */
    int size() default 0;

    /**
     * If this field is a {@link FieldType#Decimal} you can set the digit size.
     */
    int digitSize() default 0;
}
