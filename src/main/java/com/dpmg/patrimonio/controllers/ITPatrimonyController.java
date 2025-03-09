package com.dpmg.patrimonio.controllers;

import com.dpmg.patrimonio.models.dtos.ITPatrimony.FindITPatrimonyListDTO;
import com.dpmg.patrimonio.models.dtos.ITPatrimony.ITPatrimonyDTO;
import com.dpmg.patrimonio.models.dtos.ITPatrimony.SaveITPatrimonyDTO;
import com.dpmg.patrimonio.models.dtos.shared.PaginatedResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.services.ITPatrimonyService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/ti")
public class ITPatrimonyController {
    private final ITPatrimonyService itPatrimonyService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<ITPatrimonyDTO>> findAll(
            @Valid @ModelAttribute FindITPatrimonyListDTO dto
    ) {
        return ResponseEntity.ok(itPatrimonyService.findAll(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ITPatrimonyDTO>> findBydId(@PathVariable Long id) {
        return ResponseEntity.ok(itPatrimonyService.findItemDTOById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ITPatrimonyDTO>> create(@Valid @RequestBody SaveITPatrimonyDTO dto) {
        return ResponseEntity.ok(itPatrimonyService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ITPatrimonyDTO>> update(@PathVariable Long id, @Valid @RequestBody SaveITPatrimonyDTO dto) {
        return ResponseEntity.ok(itPatrimonyService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> toggleStatus(@PathVariable Long id, @Valid @RequestBody BaseAuditDTO dto) {
        return ResponseEntity.ok(itPatrimonyService.toggleStatus(id, dto));
    }
}
