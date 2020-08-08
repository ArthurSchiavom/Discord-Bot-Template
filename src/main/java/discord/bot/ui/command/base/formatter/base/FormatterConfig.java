package discord.bot.ui.command.base.formatter.base;

import discord.bot.ui.command.base.formatter.DefaultCommandFormatter;
import discord.bot.ui.command.base.formatter.DefaultMainMenuFormatter;
import discord.bot.ui.command.base.formatter.type.CommandHelpFormatter;
import discord.bot.ui.command.base.formatter.type.MainMenuFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Configurations for which formatters to use.
 */
@Configuration
public class FormatterConfig {

    @Bean
    @DependsOn("userConfigurationBootstrapper")
    public CommandHelpFormatter commandHelpFormatter() {
        return new DefaultCommandFormatter();
    }

    @Bean
    @DependsOn("userConfigurationBootstrapper")
    public MainMenuFormatter mainMenuFormatter() {
        return new DefaultMainMenuFormatter();
    }
}
