package com.dpmg.patrimonio.infra.messaging;

import com.dpmg.patrimonio.models.dtos.InventoryControl.ImportInventoryDTO;
import org.springframework.amqp.core.AmqpTemplate;

public class ImportMessageProducer {
    private AmqpTemplate amqpTemplate;

    public void integrate(ImportInventoryDTO importInventoryDTO) {
        amqpTemplate.convertAndSend(
                "importacao-request-exchange",
                "importacao-request-route-key",
                 importInventoryDTO
        );
    }
}
