package arthur.silva.discordbot.base.ui.command.formatter.base;

import arthur.silva.discordbot.base.ui.command.formatter.DefaultCommandFormatter;
import arthur.silva.discordbot.base.ui.command.formatter.DefaultMainMenuFormatter;
import arthur.silva.discordbot.base.ui.command.formatter.type.CommandHelpFormatter;
import arthur.silva.discordbot.base.ui.command.formatter.type.MainMenuFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
