package org.stockify.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.shift.ShiftRequest;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.entity.ShiftEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ShiftMapper;
import org.stockify.model.repository.ShiftRepository;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    public ShiftService(ShiftRepository shiftRepository, ShiftMapper shiftMapper) {
        this.shiftRepository = shiftRepository;
        this.shiftMapper = shiftMapper;
    }

    public ShiftResponse save(ShiftRequest shiftRequest) {
        ShiftEntity shiftEntity = shiftMapper.toEntity(shiftRequest);
        return shiftMapper.toDto(shiftRepository.save(shiftEntity));
    }

    public void delete (Long id) {
        if(!shiftRepository.existsById(id)) {
            throw new NotFoundException("Shift with ID " + id + " not found");
        }
        shiftRepository.deleteById(id);
    }

    public ShiftResponse findById(Long id) {
        ShiftEntity shiftEntity = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));
        return shiftMapper.toDto(shiftEntity);
    }

    public Page<ShiftResponse> findAll(Pageable pageable) {
        Page<ShiftEntity> shiftEntities = shiftRepository.findAll(pageable);
        Page<ShiftResponse> shiftsDto = shiftEntities.map(shiftMapper::toDto);

        return shiftsDto;
    }

    /// ES LO MISMO O NO ?¡?¡?¡?
    public ShiftResponse updateShiftPartial(Long id, ShiftRequest shiftRequest) {
        ShiftEntity existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));

        shiftMapper.partialUpdateShiftEntity(shiftRequest, existingShift);

        ShiftEntity updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDto(updatedShift);
    }

    public ShiftResponse updateShiftFull(Long id, ShiftRequest shiftRequest) {
        ShiftEntity existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + id + " not found"));

        shiftMapper.updateShiftEntity(shiftRequest, existingShift);

        ShiftEntity updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDto(updatedShift);
    }
}