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

    /**
     * The permission needed to use the command.
     */
    String permission() default "";

    /**
     * Aliases of the main command.
     */
    String[] aliases() default "";
}
