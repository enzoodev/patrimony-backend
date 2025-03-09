package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.exceptions.ItemNotFoundException;
import com.dpmg.patrimonio.models.dtos.ITPatrimony.FindITPatrimonyListDTO;
import com.dpmg.patrimonio.models.dtos.ITPatrimony.ITPatrimonyDTO;
import com.dpmg.patrimonio.models.dtos.ITPatrimony.SaveITPatrimonyDTO;
import com.dpmg.patrimonio.models.dtos.shared.PaginatedResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.ITPatrimonyEntity;
import com.dpmg.patrimonio.repositories.ITPatrimonyRepository;
import com.dpmg.patrimonio.utils.Messages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class ITPatrimonyService {
    private final ITPatrimonyRepository itPatrimonyRepository;
    private final HttpServletRequest request;

    public PaginatedResponseDTO<ITPatrimonyDTO> findAll(FindITPatrimonyListDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());

        Page<ITPatrimonyDTO> result = itPatrimonyRepository.findAll(
            dto.getPatrimonio(),
            dto.getNomeMaquina(),
            dto.getSerialMaquina(),
            dto.getSerialOffice(),
            dto.getModelo(),
            dto.getVersaoOffice(),
            dto.getSistemaOperacional(),
            dto.getStatus(),
            pageable
        );

        String message = result.isEmpty() ? Messages.EMPTY_LIST : Messages.SUCCESS_FETCH;
        return PaginatedResponseDTO.fromPage(message, result);
    }

    public ResponseDTO<ITPatrimonyDTO> findItemDTOById(Long id) {
        ITPatrimonyDTO itPatrimony = itPatrimonyRepository.findItemDTOById(id);

        if (itPatrimony == null) {
            throw new ItemNotFoundException();
        }

        return new ResponseDTO<>(Messages.SUCCESS_FETCH, itPatrimony);
    }

    private ITPatrimonyEntity findById(Long id) {
        Optional<ITPatrimonyEntity> itPatrimony = itPatrimonyRepository.findById(id);

        if (itPatrimony.isEmpty()) {
            throw new ItemNotFoundException();
        }

        return itPatrimony.get();
    }

    @Transactional
    public ResponseDTO<ITPatrimonyDTO> create(SaveITPatrimonyDTO dto) {
        ITPatrimonyEntity itPatrimonyEntity = new ITPatrimonyEntity();

        itPatrimonyEntity.setNumeroPatrimonio(dto.getNumPatrimonio());
        itPatrimonyEntity.setMaquina(dto.getNomeMaquina());
        itPatrimonyEntity.setModelo(dto.getModelo());
        itPatrimonyEntity.setMarca(dto.getMarca());
        itPatrimonyEntity.setVersaoOffice(dto.getVersaoOffice());
        itPatrimonyEntity.setSistemaOperacional(dto.getSistemaOperacional());
        itPatrimonyEntity.setSerialMaquina(dto.getSerialMaquina());
        itPatrimonyEntity.setSerialWindows(dto.getSerialWindows());
        itPatrimonyEntity.setSerialOffice(dto.getSerialOffice());
        itPatrimonyEntity.setProcessador(dto.getProcessador());
        itPatrimonyEntity.setMemoria(dto.getMemoria());
        itPatrimonyEntity.setIsAtivo(dto.getAtivo());
        itPatrimonyEntity.setLotacao(dto.getLotacao());
        itPatrimonyEntity.setHd(dto.getHd());
        itPatrimonyEntity.setMacAddress(dto.getMacAdress());
        itPatrimonyEntity.setResponsavel(dto.getNomeResponsavel());
        itPatrimonyEntity.setObservacao(dto.getObservacao());

        itPatrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        itPatrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        itPatrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        // TODO: change it in the future
        itPatrimonyEntity.setUuidUsuario("TESTE DO ENZO");

        itPatrimonyRepository.save(itPatrimonyEntity);

        ITPatrimonyDTO itPatrimony = itPatrimonyRepository.findItemDTOById(itPatrimonyEntity.getId());
        return new ResponseDTO<>(Messages.SUCCESS_CREATED_PATRIMONY, itPatrimony);
    }

    @Transactional
    public ResponseDTO<ITPatrimonyDTO> update(Long id, SaveITPatrimonyDTO dto) {
        ITPatrimonyEntity itPatrimonyEntity = findById(id);

        itPatrimonyEntity.setNumeroPatrimonio(dto.getNumPatrimonio());
        itPatrimonyEntity.setMaquina(dto.getNomeMaquina());
        itPatrimonyEntity.setModelo(dto.getModelo());
        itPatrimonyEntity.setMarca(dto.getMarca());
        itPatrimonyEntity.setVersaoOffice(dto.getVersaoOffice());
        itPatrimonyEntity.setSistemaOperacional(dto.getSistemaOperacional());
        itPatrimonyEntity.setSerialMaquina(dto.getSerialMaquina());
        itPatrimonyEntity.setSerialWindows(dto.getSerialWindows());
        itPatrimonyEntity.setSerialOffice(dto.getSerialOffice());
        itPatrimonyEntity.setProcessador(dto.getProcessador());
        itPatrimonyEntity.setMemoria(dto.getMemoria());
        itPatrimonyEntity.setIsAtivo(dto.getAtivo());
        itPatrimonyEntity.setLotacao(dto.getLotacao());
        itPatrimonyEntity.setHd(dto.getHd());
        itPatrimonyEntity.setMacAddress(dto.getMacAdress());
        itPatrimonyEntity.setResponsavel(dto.getNomeResponsavel());
        itPatrimonyEntity.setObservacao(dto.getObservacao());

        itPatrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        itPatrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        itPatrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        itPatrimonyRepository.save(itPatrimonyEntity);

        ITPatrimonyDTO itPatrimony = itPatrimonyRepository.findItemDTOById(id);
        return new ResponseDTO<>(Messages.SUCCESS_UPDATED_PATRIMONY, itPatrimony);
    }

    @Transactional
    public ResponseDTO<Void> toggleStatus(Long id, BaseAuditDTO dto) {
        ITPatrimonyEntity itPatrimonyEntity = findById(id);

        Boolean isActive = itPatrimonyEntity.getIsAtivo();
        itPatrimonyEntity.setIsAtivo(!isActive);
        itPatrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        itPatrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        itPatrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        itPatrimonyRepository.save(itPatrimonyEntity);

        return new ResponseDTO<>(Messages.SUCCESS_UPDATED_PATRIMONY, null);
    }
}
