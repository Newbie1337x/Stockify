package org.stockify.config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Component
public class GlobalPreferencesConfig {

    @Getter
    private boolean mailEnabled = true;
    @Getter
    private boolean stockAlertEnabled = true;
    @Getter
    private double stockAlertThreshold = 10;
    @Getter
    private String emailAddress = "example@example";

    private static final String FILE_NAME = "user-preferences.json";
    private static final String FILE_PATH = "src/main/resources/" + FILE_NAME;
    private static final Logger LOGGER = Logger.getLogger(GlobalPreferencesConfig.class.getName());

    public boolean shouldSendStockAlert(double stockPrice,boolean mailAlreadySended) {
        return stockPrice < stockAlertThreshold && stockAlertEnabled && mailEnabled && !mailAlreadySended;
    }

    @PostConstruct
    public void loadPreferences() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(FILE_PATH);

            GlobalPreferencesConfig defaults = new GlobalPreferencesConfig();

            if (file.exists()) {
                JsonNode fileJson = mapper.readTree(file);
                ObjectNode merged = mapper.valueToTree(defaults);
                merged.setAll((ObjectNode) fileJson);

                GlobalPreferencesConfig loadedPrefs = mapper.treeToValue(merged, GlobalPreferencesConfig.class);

                this.mailEnabled = loadedPrefs.mailEnabled;
                this.stockAlertThreshold = loadedPrefs.stockAlertThreshold;
                this.stockAlertEnabled = loadedPrefs.stockAlertEnabled;
                this.emailAddress = loadedPrefs.emailAddress;

                // Reescribir el archivo si faltaban campos
                if (merged.size() < mapper.valueToTree(defaults).size()) {
                    savePreferencesToFile(mapper, merged);
                }
            } else {
                LOGGER.warning(FILE_NAME + " not found. Creating with default values...");
                savePreferencesToFile(mapper, mapper.valueToTree(defaults));
            }

        } catch (Exception e) {
            LOGGER.severe("Failed to load or create preferences: " + e.getMessage());
        }
    }

    private void savePreferencesToFile(ObjectMapper mapper, JsonNode node) {
        try {
            Files.createDirectories(Paths.get("src/main/resources"));
            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                String updatedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
                fos.write(updatedJson.getBytes());
                LOGGER.info("Updated " + FILE_NAME + " with default values for missing fields.");
            }
        } catch (Exception ex) {
            LOGGER.severe("Failed to write " + FILE_NAME + ": " + ex.getMessage());
        }
    }
}
