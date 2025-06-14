package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.stockify.config.GlobalPreferencesConfig;
import org.stockify.model.entity.StockEntity;

@RequiredArgsConstructor

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final GlobalPreferencesConfig globalPreferencesConfig;

    /**
     * Envia un correo electrónico con la información proporcionada.
     * 
     * @param cause Asunto del correo electrónico
     * @param description Cuerpo del correo electrónico
     */
    public void sendEmail(String cause, String description){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(globalPreferencesConfig.getEmailAddress());
        message.setSubject(cause);
        message.setText(description);
        mailSender.send(message);
    }

    /**
     * Envia una alerta por correo electrónico cuando el stock de un producto está por debajo del umbral configurado.
     * Solo envía la alerta si se cumplen las condiciones configuradas en las preferencias globales
     * y si no se ha enviado una alerta previamente para este stock.
     * 
     * @param stock Entidad de stock que contiene la información del producto y su cantidad actual
     */
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
