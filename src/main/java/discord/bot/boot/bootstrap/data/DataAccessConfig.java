package discord.bot.boot.bootstrap.data;

import discord.bot.data.database.PlaceholderRepo_TEMP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

@Configuration
@EnableJpaRepositories(basePackages = {"discord.bot.data.database"})
@EnableTransactionManagement
@EntityScan(basePackages = {"discord.bot.domain"})
@EnableAspectJAutoProxy
public class DataAccessConfig {
    Logger logger = LoggerFactory.getLogger(DataAccessConfig.class);

    @Autowired
    PlaceholderRepo_TEMP repo;

    @PostConstruct
    public void testDatabaseInitialization() {
        try {
            repo.count();
            logger.info("Successfully connected to the database.");
        } catch (Exception e) {
            logger.error("FAILED TO ACCESS THE DATABASE. " +
                    "The database might be unavailable or the access information might be wrong. Details:" + e.getMessage());
        }
    }

}
