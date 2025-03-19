package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.exceptions.*;
import com.dpmg.patrimonio.models.dtos.Patrimony.*;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.FindAllPatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.PatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.SavePatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.dtos.shared.PaginatedResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.PatrimonyEntity;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import com.dpmg.patrimonio.repositories.PatrimonyRepository;
import com.dpmg.patrimonio.utils.Messages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class PatrimonyService {
    private final PatrimonyRepository patrimonyRepository;
    private final InventoryControlService inventoryControlService;
    private final HttpServletRequest request;

    public ResponseDTO<List<UnitDTO>> findResponsibleUnitListByInventoryId(Long id) {
        List<UnitDTO> unitList = patrimonyRepository.findResponsibleUnitListByInventoryId(id);
        String message = unitList.isEmpty() ? Messages.EMPTY_LIST : Messages.SUCCESS_FETCH;
        return new ResponseDTO<>(message, unitList);
    }

    private UnitDTO findUnitByInventoryIdAndUnitNumber(Long inventoryId, Long unitNumber) {
        UnitDTO unit = patrimonyRepository.findResponsibleUnitByInventoryIdAndUnitNumber(inventoryId, unitNumber);

        if (unit == null) {
            throw new UnitNotFoundException();
        }

        return unit;
    }

    private void verifyIfExistsUnitByInventoryIdAndUnitNumber(Long inventoryId, Long unitNumber) {
        if (Boolean.FALSE.equals(patrimonyRepository.existsUnitByInventoryIdAndUnitNumber(inventoryId, unitNumber))) {
            throw new UnitNotFoundException();
        }
    }

    public PaginatedResponseDTO<PatrimonyDTO> findAll(FindAllPatrimonyListDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        Long inventoryId = dto.getIdInventario();
        Long unitCode = dto.getCodUnidadeResponsavel();

        if (unitCode != null) {
            verifyIfExistsUnitByInventoryIdAndUnitNumber(inventoryId, unitCode);
        }

        Page<PatrimonyDTO> result = patrimonyRepository.findByInventoryId(
                dto.getNumeroPatrimonio(),
                dto.getSituacao(),
                unitCode,
                dto.getDescricaoItemMaterial(),
                inventoryId,
                pageable
        );

        String message = result.isEmpty() ? Messages.EMPTY_LIST : Messages.SUCCESS_FETCH;
        return PaginatedResponseDTO.fromPage(message, result);
    }

    private PatrimonyEntity findById(Long id) {
        Optional<PatrimonyEntity> patrimony = patrimonyRepository.findById(id);

        if (patrimony.isEmpty() || Boolean.FALSE.equals(patrimony.get().getIsAtivo())) {
            throw new ItemNotFoundException();
        }

        return patrimony.get();
    }

    public ResponseDTO<List<String>> findDescriptionsByInventoryId(Long inventoryId) {
        List<String> descriptions = patrimonyRepository.findItemDescriptionsByInventoryId(inventoryId);
        String message = descriptions.isEmpty() ? Messages.EMPTY_LIST : Messages.SUCCESS_FETCH;

        return new ResponseDTO<>(message, descriptions);
    }

    public ResponseDTO<PatrimonyDetailsDTO> findItemDetailsById(Long id) {
        PatrimonyDetailsDTO details = patrimonyRepository.findItemDetailsById(id);

        if (details == null) {
            throw new ItemNotFoundException();
        }

        return new ResponseDTO<>(Messages.SUCCESS_FETCH, details);
    }

    public ResponseDTO<PatrimonyObservationDTO> findItemObservationById(Long id) {
        PatrimonyObservationDTO observation = patrimonyRepository.findItemObservationById(id);

        if (observation == null) {
            throw new ItemNotFoundException();
        }

        return new ResponseDTO<>(Messages.SUCCESS_FETCH, observation);
    }

    @Transactional
    public ResponseDTO<Void> updateItemObservation(Long id, UpdatePatrimonyObservationDTO dto) {
        PatrimonyEntity patrimonyEntity = this.findById(id);

        inventoryControlService.verifyIfIsOpenById(patrimonyEntity.getInventario().getId());

        patrimonyEntity.setObservacao(dto.getObservacao());
        patrimonyEntity.setSala(dto.getSala());

        patrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        patrimonyRepository.save(patrimonyEntity);

        return new ResponseDTO<>(Messages.SUCCESS_SAVE_OBSERVATION, null);
    }

    @Transactional
    public void updateItemSituation(Long id, UpdatePatrimonySituationDTO dto) {
        PatrimonySituationEnum situation = dto.getSituacao();

        if (situation != PatrimonySituationEnum.LOCALIZADO && situation != PatrimonySituationEnum.NAO_LOCALIZADO) {
            throw new InvalidStatusException();
        }

        PatrimonyEntity patrimonyEntity = this.findById(id);

        inventoryControlService.verifyIfIsOpenById(patrimonyEntity.getInventario().getId());

        patrimonyEntity.setSituacao(situation);
        patrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        patrimonyRepository.save(patrimonyEntity);
    }

    public PaginatedResponseDTO<PatrimonyOtherSituationDTO> findOtherSituations(FindAllPatrimonyOtherSituationDTO dto) {
        Pageable page = PageRequest.of(dto.getPage() - 1, dto.getSize());
        Long inventoryId = dto.getIdInventario();
        Long unitCode = dto.getCodUnidadeResponsavel();

        if (unitCode != null) {
            verifyIfExistsUnitByInventoryIdAndUnitNumber(inventoryId, unitCode);
        }

        Page<PatrimonyOtherSituationDTO> result = patrimonyRepository.findOtherSituations(
                dto.getSituacao(),
                dto.getNumeroPatrimonio(),
                unitCode,
                dto.getDescricaoItemMaterial(),
                inventoryId,
                page
        );

        String message = result.isEmpty() ? Messages.EMPTY_LIST : Messages.SUCCESS_FETCH;
        return PaginatedResponseDTO.fromPage(message, result);
    }

    public ResponseDTO<PatrimonyOtherSituationDTO> findOtherSituationById(Long id) {
        PatrimonyOtherSituationDTO patrimonyOtherSituation = patrimonyRepository.findOtherSituationById(id);

        if (patrimonyOtherSituation == null) {
            throw new ItemNotFoundException();
        }

        return new ResponseDTO<>(Messages.SUCCESS_FETCH, patrimonyOtherSituation);
    }

    private void verifyIfCanUpdatePatrimonyOtherSituation(PatrimonyEntity patrimonyEntity) {
        inventoryControlService.verifyIfIsOpenById(patrimonyEntity.getInventario().getId());

        if (Boolean.FALSE.equals(patrimonyEntity.getIsOutraSituacao())) {
            throw new InvalidOtherSituationIdException(patrimonyEntity.getId());
        }

        if (Boolean.FALSE.equals(patrimonyEntity.getIsCadastroManual())) {
            throw new NotManualRegistrationException();
        }
    }

    @Transactional
    public ResponseDTO<PatrimonyOtherSituationDTO> createOtherSituation(SavePatrimonyOtherSituationDTO dto) {
        inventoryControlService.verifyIfIsOpenById(dto.getIdInventario());
        UnitDTO unit = findUnitByInventoryIdAndUnitNumber(dto.getIdInventario(), dto.getCodUnidadeResponsavel());

        PatrimonyEntity patrimonyEntity = new PatrimonyEntity();

        patrimonyEntity.setIsOutraSituacao(true);
        patrimonyEntity.setIsCadastroManual(true);
        patrimonyEntity.setSituacao(dto.getSituacao());
        patrimonyEntity.setNumeroPatrimonio(dto.getNumeroPatrimonio());
        patrimonyEntity.setDescricaoItemMaterial(dto.getDescricaoItemMaterial());
        patrimonyEntity.setCodigoUnidadeResponsavel(unit.getCodigo());
        patrimonyEntity.setNomeUnidadeResponsavel(unit.getNome());
        patrimonyEntity.setCodigoUnidadeEncontrado(unit.getCodigo());
        patrimonyEntity.setNomeUnidadeEncontrado(unit.getNome());

        patrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        // TODO: change it in the future
        patrimonyEntity.setUuidUsuario("TESTE DO ENZO");

        patrimonyRepository.save(patrimonyEntity);
        PatrimonyOtherSituationDTO patrimonyOtherSituation = patrimonyRepository.findOtherSituationById(patrimonyEntity.getId());

        return new ResponseDTO<>(Messages.SUCCESS_CREATED_PATRIMONY, patrimonyOtherSituation);
    }

    @Transactional
    public ResponseDTO<PatrimonyOtherSituationDTO> updateOtherSituation(Long id, SavePatrimonyOtherSituationDTO dto) {
        inventoryControlService.verifyIfIsOpenById(dto.getIdInventario());

        UnitDTO unit = findUnitByInventoryIdAndUnitNumber(dto.getIdInventario(), dto.getCodUnidadeResponsavel());

        PatrimonyEntity patrimonyEntity = findById(id);
        verifyIfCanUpdatePatrimonyOtherSituation(patrimonyEntity);

        patrimonyEntity.setSituacao(dto.getSituacao());
        patrimonyEntity.setNumeroPatrimonio(dto.getNumeroPatrimonio());
        patrimonyEntity.setDescricaoItemMaterial(dto.getDescricaoItemMaterial());
        patrimonyEntity.setCodigoUnidadeResponsavel(unit.getCodigo());
        patrimonyEntity.setNomeUnidadeResponsavel(unit.getNome());
        patrimonyEntity.setCodigoUnidadeEncontrado(unit.getCodigo());
        patrimonyEntity.setNomeUnidadeEncontrado(unit.getNome());

        patrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        patrimonyRepository.save(patrimonyEntity);
        PatrimonyOtherSituationDTO patrimonyOtherSituation = patrimonyRepository.findOtherSituationById(id);

        return new ResponseDTO<>(Messages.SUCCESS_UPDATED_PATRIMONY, patrimonyOtherSituation);
    }

    @Transactional
    public ResponseDTO<Void> deactivatePatrimonyOtherSituation(Long id, BaseAuditDTO dto) {
        PatrimonyEntity patrimonyEntity = findById(id);
        verifyIfCanUpdatePatrimonyOtherSituation(patrimonyEntity);

        patrimonyEntity.setIsAtivo(false);
        patrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        patrimonyRepository.save(patrimonyEntity);

        return new ResponseDTO<>(Messages.SUCCESS_DELETED_PATRIMONY, null);
    }

    private ResponseDTO<PatrimonyToBeLocalizedDTO> findPatrimonyInTheSameUnit(Long inventoryId, Long unitCode, Long patrimonyNumber) {
        PatrimonyToBeLocalizedDTO patrimonyInTheSameUnitDTO = patrimonyRepository.findByInventoryIdAndUnitNumberAndPatrimonyNumber(
                inventoryId,
                unitCode,
                patrimonyNumber
        );

        if (patrimonyInTheSameUnitDTO == null) {
            return null;
        }

        if (patrimonyInTheSameUnitDTO.getSituacaoAtual() == PatrimonySituationEnum.LOCALIZADO) {
            return new ResponseDTO<>(String.format(Messages.DUPLICATED_ITEM, patrimonyNumber), patrimonyInTheSameUnitDTO);
        }

        patrimonyInTheSameUnitDTO.setSituacaoFutura(PatrimonySituationEnum.LOCALIZADO);
        return new ResponseDTO<>(Messages.SUCCESS_FETCH, patrimonyInTheSameUnitDTO);
    }

    private void createOtherSituationForLocalizedInOtherUnit(
            PatrimonyEntity existingPatrimonyEntity,
            UnitDTO responsibleUnit,
            UnitDTO foundedUnit,
            String sgProjetoModificador,
            String sgAcaoModificadora,
            String requestURL
    ) {
        PatrimonyEntity patrimonyEntity = new PatrimonyEntity();

        patrimonyEntity.setInventario(existingPatrimonyEntity.getInventario());
        patrimonyEntity.setSituacao(PatrimonySituationEnum.OUTRA_UNIDADE);
        patrimonyEntity.setIsOutraSituacao(true);
        patrimonyEntity.setIsPatrimonioForaDaUnidade(true);
        patrimonyEntity.setIsCadastroManual(false);
        patrimonyEntity.setNumeroPatrimonio(existingPatrimonyEntity.getNumeroPatrimonio());
        patrimonyEntity.setCodigoUnidadeResponsavel(responsibleUnit.getCodigo());
        patrimonyEntity.setNomeUnidadeResponsavel(responsibleUnit.getNome());
        patrimonyEntity.setCodigoUnidadeEncontrado(foundedUnit.getCodigo());
        patrimonyEntity.setNomeUnidadeEncontrado(foundedUnit.getNome());
        patrimonyEntity.setDescricaoItemMaterial(existingPatrimonyEntity.getDescricaoItemMaterial());

        patrimonyEntity.setSgProjetoModificador(sgProjetoModificador);
        patrimonyEntity.setSgAcaoModificadora(sgAcaoModificadora);
        patrimonyEntity.setNoEndPointModificador(requestURL);
        patrimonyEntity.setUuidUsuario("TESTE DO ENZO");

        patrimonyRepository.save(patrimonyEntity);
    }

    private ResponseDTO<PatrimonyToBeLocalizedDTO> findPatrimonyInOtherUnit(
            Long inventoryId,
            Long unitCode,
            Long patrimonyNumber,
            String sgProjetoModificador,
            String sgAcaoModificadora
    ) {
        PatrimonyToBeLocalizedDTO patrimonyInOtherUnitDTO = patrimonyRepository.findByInventoryIdAndPatrimonyNumber(
                inventoryId,
                unitCode
        );

        if (patrimonyInOtherUnitDTO == null) {
            return null;
        }

        String requestURL = request.getRequestURL().toString();

        PatrimonyEntity existingPatrimonyEntity = findById(patrimonyInOtherUnitDTO.getId());
        UnitDTO responsibleUnit = findUnitByInventoryIdAndUnitNumber(inventoryId, existingPatrimonyEntity.getCodigoUnidadeResponsavel());
        UnitDTO foundedUnit = findUnitByInventoryIdAndUnitNumber(inventoryId, unitCode);

        patrimonyInOtherUnitDTO.setUnidadeEncontrado(foundedUnit);
        patrimonyInOtherUnitDTO.setSituacaoFutura(PatrimonySituationEnum.OUTRA_UNIDADE);

        existingPatrimonyEntity.setSituacao(PatrimonySituationEnum.LOCALIZADO);
        existingPatrimonyEntity.setIsPatrimonioForaDaUnidade(true);
        existingPatrimonyEntity.setCodigoUnidadeEncontrado(foundedUnit.getCodigo());
        existingPatrimonyEntity.setNomeUnidadeEncontrado(foundedUnit.getNome());

        existingPatrimonyEntity.setSgProjetoModificador(sgProjetoModificador);
        existingPatrimonyEntity.setSgAcaoModificadora(sgAcaoModificadora);
        existingPatrimonyEntity.setNoEndPointModificador(requestURL);

        patrimonyRepository.save(existingPatrimonyEntity);

        createOtherSituationForLocalizedInOtherUnit(
            existingPatrimonyEntity,
            responsibleUnit,
            foundedUnit,
            sgProjetoModificador,
            sgAcaoModificadora,
            requestURL
        );

        String message = String.format(
                Messages.LOCALIZED_IN_OTHER_UNIT,
                patrimonyNumber,
                existingPatrimonyEntity.getDescricaoItemMaterial(),
                responsibleUnit.getNome(),
                foundedUnit.getNome()
        );
        return new ResponseDTO<>(message, patrimonyInOtherUnitDTO);
    }

    @Transactional
    public ResponseDTO<PatrimonyToBeLocalizedDTO> verifyIfPatrimonyIsReadToBeLocalized(VerifyIfPatrimonyIsReadToBeLocalizedDTO dto) {
        Long inventoryId = dto.getIdInventario();
        Long unitCode = dto.getCodigoUnidadeResponsavel();
        Long patrimonyNumber = dto.getNumeroPatrimonio();

        inventoryControlService.verifyIfIsOpenById(inventoryId);
        verifyIfExistsUnitByInventoryIdAndUnitNumber(inventoryId, unitCode);

        ResponseDTO<PatrimonyToBeLocalizedDTO> patrimonyInTheSameUnit = findPatrimonyInTheSameUnit(inventoryId, unitCode, patrimonyNumber);

        if (patrimonyInTheSameUnit != null) {
            return patrimonyInTheSameUnit;
        }

        ResponseDTO<PatrimonyToBeLocalizedDTO> patrimonyInOtherUnit = findPatrimonyInOtherUnit(
                inventoryId,
                unitCode,
                patrimonyNumber,
                dto.getSgProjetoModificador(),
                dto.getSgAcaoModificadora()
        );

        if (patrimonyInOtherUnit != null) {
            return patrimonyInOtherUnit;
        }

        return new ResponseDTO<>(Messages.ITEM_NOT_FOUND_IN_INVENTORY, null);
    }

    @Transactional
    public ResponseDTO<Void> localizePatrimony(Long id, UpdatePatrimonyObservationDTO dto) {
        PatrimonyEntity patrimonyEntity = findById(id);

        inventoryControlService.verifyIfIsOpenById(patrimonyEntity.getInventario().getId());

        if (patrimonyEntity.getSituacao() == PatrimonySituationEnum.LOCALIZADO) {
            throw new ThisItemWasAlreadyLocatedException(patrimonyEntity.getNumeroPatrimonio());
        }

        patrimonyEntity.setSituacao(PatrimonySituationEnum.LOCALIZADO);
        patrimonyEntity.setSala(dto.getSala());
        patrimonyEntity.setObservacao(dto.getObservacao());

        patrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        patrimonyRepository.save(patrimonyEntity);

        return new ResponseDTO<>(Messages.SUCCESS_UPDATED_PATRIMONY, null);
    }
}
