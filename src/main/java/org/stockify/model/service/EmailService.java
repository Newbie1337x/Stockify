package org.stockify.model.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.stockify.config.GlobalPreferencesConfig;
import org.stockify.model.entity.StockEntity;
import org.stockify.model.entity.TransactionEntity;

@AllArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final GlobalPreferencesConfig globalPreferencesConfig;

    public void sendEmail(String cause, String description){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(globalPreferencesConfig.getEmailAddress());
        message.setSubject(cause);
        message.setText(description);
        mailSender.send(message);
    }

    public void sendStockAlert(StockEntity stock) {
        if (globalPreferencesConfig.shouldSendStockAlert(stock.getQuantity(),stock.isLowStockAlertSent())) {
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




