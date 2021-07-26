package com.github.jadepeng.pipeline.service.impl;

import com.github.jadepeng.pipeline.domain.ProgramVersion;
import com.github.jadepeng.pipeline.repository.ProgramVersionRepository;
import com.github.jadepeng.pipeline.service.ProgramVersionService;
import com.github.jadepeng.pipeline.service.dto.ProgramVersionDTO;
import com.github.jadepeng.pipeline.service.mapper.ProgramVersionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ProgramVersion}.
 */
@Service
public class ProgramVersionServiceImpl implements ProgramVersionService {

    private final Logger log = LoggerFactory.getLogger(ProgramVersionServiceImpl.class);

    private final ProgramVersionRepository programVersionRepository;

    private final ProgramVersionMapper programVersionMapper;

    public ProgramVersionServiceImpl(ProgramVersionRepository programVersionRepository, ProgramVersionMapper programVersionMapper) {
        this.programVersionRepository = programVersionRepository;
        this.programVersionMapper = programVersionMapper;
    }

    @Override
    public ProgramVersionDTO save(ProgramVersionDTO programVersionDTO) {
        log.debug("Request to save ProgramVersion : {}", programVersionDTO);
        ProgramVersion programVersion = programVersionMapper.toEntity(programVersionDTO);
        programVersion = programVersionRepository.save(programVersion);
        return programVersionMapper.toDto(programVersion);
    }

    @Override
    public Optional<ProgramVersionDTO> partialUpdate(ProgramVersionDTO programVersionDTO) {
        log.debug("Request to partially update ProgramVersion : {}", programVersionDTO);

        return programVersionRepository
            .findById(programVersionDTO.getId())
            .map(
                existingProgramVersion -> {
                    programVersionMapper.partialUpdate(existingProgramVersion, programVersionDTO);

                    return existingProgramVersion;
                }
            )
            .map(programVersionRepository::save)
            .map(programVersionMapper::toDto);
    }

    @Override
    public Page<ProgramVersionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProgramVersions");
        return programVersionRepository.findAll(pageable).map(programVersionMapper::toDto);
    }

    @Override
    public Optional<ProgramVersionDTO> findOne(String id) {
        log.debug("Request to get ProgramVersion : {}", id);
        return programVersionRepository.findById(id).map(programVersionMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ProgramVersion : {}", id);
        programVersionRepository.deleteById(id);
    }
}
