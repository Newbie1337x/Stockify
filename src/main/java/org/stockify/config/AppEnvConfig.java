package org.stockify.config;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.NoArgsConstructor;
import java.util.Objects;

@NoArgsConstructor
public final class AppEnvConfig {

    public static void loadEnv() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_URL", Objects.requireNonNull(dotenv.get("DB_URL")));
        System.setProperty("DB_USER", Objects.requireNonNull(dotenv.get("DB_USER")));
        System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
        System.setProperty("JWT_SECRET", Objects.requireNonNull(dotenv.get("JWT_SECRET")));
        System.setProperty("JWT_EXPIRATION", Objects.requireNonNull(dotenv.get("JWT_EXPIRATION")));
        System.setProperty("EMAIL_HOST", Objects.requireNonNull(dotenv.get("EMAIL_HOST")));
        System.setProperty("EMAIL_PORT", Objects.requireNonNull(dotenv.get("EMAIL_PORT")));
        System.setProperty("EMAIL_USERNAME", Objects.requireNonNull(dotenv.get("EMAIL_USERNAME")));
        System.setProperty("EMAIL_PASSWORD", Objects.requireNonNull(dotenv.get("EMAIL_PASSWORD")));
    }
}
