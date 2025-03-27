package com.dpmg.patrimonio.controllers;

import com.dpmg.patrimonio.models.dtos.Inventory.FindInventoryByYearDTO;
import com.dpmg.patrimonio.models.dtos.Inventory.InventoryDTO;
import com.dpmg.patrimonio.models.dtos.Inventory.UpdateInventoryStatusDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.models.enums.InventorySituationEnum;
import com.dpmg.patrimonio.services.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/inventarios")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ResponseDTO<InventoryDTO>> findByYear(@Valid @ModelAttribute FindInventoryByYearDTO dto) {
        return ResponseEntity.ok(inventoryService.findDTOByYear(dto.getAno()));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<InventorySituationEnum>> importInventory(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sgProjetoModificador") String sgProjetoModificador,
            @RequestParam("sgAcaoModificadora") String sgAcaoModificadora
    ) {
        return ResponseEntity.ok(inventoryService.importInventory(file, sgProjetoModificador, sgAcaoModificadora));
    }

    @PatchMapping("/status")
    public ResponseEntity<ResponseDTO<InventorySituationEnum>> updateStatus(@Valid @RequestBody UpdateInventoryStatusDTO dto) {
        return ResponseEntity.ok(inventoryService.updateStatus(dto));
    }
}
