package academy.devdojo.config;

import connection.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {

    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
    @Profile(value = "mongo")
    public Connection connectionMongoDB() {
        return new Connection(
                configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }

    @Bean
    @Profile(value = "mysql")
    public Connection connectionMySQL() {
        return new Connection(
                configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }

    @Bean
    public Connection connection() {
        return new Connection(
                configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }
}
