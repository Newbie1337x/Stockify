package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.stockify.config.GlobalPreferencesConfig;
import org.stockify.model.entity.StockEntity;

/**
 * Service responsible for sending email notifications.
 * <p>
 * Provides methods to send general emails and specific stock alert emails based on configured global preferences.
 * </p>
 */
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final GlobalPreferencesConfig globalPreferencesConfig;

    /**
     * Sends an email with the specified subject and body.
     *
     * @param cause       The subject of the email.
     * @param description The body content of the email.
     */
    public void sendEmail(String cause, String description) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(globalPreferencesConfig.getEmailAddress());
        message.setSubject(cause);
        message.setText(description);
        mailSender.send(message);
    }

    /**
     * Sends a low-stock alert email if the stock quantity is below the configured threshold,
     * and an alert has not been sent previously for this stock.
     * <p>
     * This method checks global preferences to determine whether the alert should be sent.
     * Once sent, it marks the stock entity to prevent duplicate alerts.
     * </p>
     *
     * @param stock The StockEntity containing product and current quantity information.
     */
    public void sendStockAlert(StockEntity stock) {
        if (globalPreferencesConfig.shouldSendStockAlert(stock.getQuantity(), stock.isLowStockAlertSent())) {
            String subject = "Low stock alert: " + stock.getProduct().getName();
            String body = String.format("""
                    Product: %s
                    Store: %s
                    %s
                    Current quantity: %.2f
                    Minimum expected: %.2f
                    """,
                    stock.getProduct().getName(),
                    stock.getStore().getStoreName(),
                    stock.getStore().getAddress(),
                    stock.getQuantity(),
                    globalPreferencesConfig.getStockAlertThreshold()
            );
            stock.setLowStockAlertSent(true);
            sendEmail(subject, body);
        }
    }
}
