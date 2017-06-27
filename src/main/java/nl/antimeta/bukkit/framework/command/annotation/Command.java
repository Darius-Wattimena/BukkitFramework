package nl.antimeta.bukkit.framework.command.annotation;

import nl.antimeta.bukkit.framework.command.MainCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    /**
     * Use this when you want to instantly call the command without using the {@link MainCommand}.
     */
    String main();

    /**
     * Command will be called when calling one of the given subcommands.
     */
    String[] subcommands();

    /**
     * The permission needed to use the command.
     */
    String permission() default "";

    /**
     * Aliases of the main command.
     */
    String[] aliases() default "";
}
