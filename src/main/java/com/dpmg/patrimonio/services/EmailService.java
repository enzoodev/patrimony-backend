package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.models.entities.InventoryControlEntity;
import com.dpmg.patrimonio.utils.Messages;
import lombok.Data;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Data
@Component
public class EmailService {
    private final JavaMailSender mailSender;

    private void sendSimpleEmail(String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("enzodm.dev@gmail.com");
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("test.enzo.spring@gmail.com");

        mailSender.send(message);
    }

    public void sendImportCompletionEmail(InventoryControlEntity inventoryControlEntity) {
        String text = String.format(Messages.IMPORT_COMPLETED_FULL_MESSAGE, inventoryControlEntity.getAno(), inventoryControlEntity.getListaPatrimonio().size());
        sendSimpleEmail(Messages.IMPORT_COMPLETED, text);
    }

    public void sendImportFailureEmail(String text) {
        sendSimpleEmail(Messages.FAILED_TO_IMPORT_FILE, text);
    }
}
