package arthur.silva.discordbot.base.ui.command.base.formatter.base;

import arthur.silva.discordbot.base.ui.command.base.formatter.DefaultCommandFormatter;
import arthur.silva.discordbot.base.ui.command.base.formatter.DefaultMainMenuFormatter;
import arthur.silva.discordbot.base.ui.command.base.formatter.type.CommandHelpFormatter;
import arthur.silva.discordbot.base.ui.command.base.formatter.type.MainMenuFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurations for which formatters to use.
 */
@Configuration
public class FormatterConfig {

    @Bean
    public CommandHelpFormatter commandHelpFormatter() {
        return new DefaultCommandFormatter();
    }

    @Bean
    public MainMenuFormatter mainMenuFormatter() {
        return new DefaultMainMenuFormatter();
    }
}
