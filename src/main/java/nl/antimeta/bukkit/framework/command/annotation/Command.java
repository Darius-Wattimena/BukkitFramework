package nl.antimeta.bukkit.framework.command.annotation;

import nl.antimeta.bukkit.framework.command.MainCommand;

public @interface Command {

    /**
     * Use this when you want to instantly call the command without using the {@link MainCommand}.
     */
    String main() default "";

    /**
     * Command will be called when calling one of the given subcommands.
     */
    String[] subcommands() default "";


}
