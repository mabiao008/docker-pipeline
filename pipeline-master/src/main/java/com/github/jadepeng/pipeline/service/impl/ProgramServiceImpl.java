package com.github.jadepeng.pipeline.service.impl;

import com.github.jadepeng.pipeline.domain.Program;
import com.github.jadepeng.pipeline.repository.ProgramRepository;
import com.github.jadepeng.pipeline.service.ProgramService;
import com.github.jadepeng.pipeline.service.dto.ProgramDTO;
import com.github.jadepeng.pipeline.service.mapper.ProgramMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Program}.
 */
@Service
public class ProgramServiceImpl implements ProgramService {

    private final Logger log = LoggerFactory.getLogger(ProgramServiceImpl.class);

    private final ProgramRepository programRepository;

    private final ProgramMapper programMapper;

    public ProgramServiceImpl(ProgramRepository programRepository, ProgramMapper programMapper) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
    }

    @Override
    public ProgramDTO save(ProgramDTO programDTO) {
        log.debug("Request to save Program : {}", programDTO);
        Program program = programMapper.toEntity(programDTO);
        program = programRepository.save(program);
        return programMapper.toDto(program);
    }

    @Override
    public Optional<ProgramDTO> partialUpdate(ProgramDTO programDTO) {
        log.debug("Request to partially update Program : {}", programDTO);

        return programRepository
            .findById(programDTO.getId())
            .map(
                existingProgram -> {
                    programMapper.partialUpdate(existingProgram, programDTO);

                    return existingProgram;
                }
            )
            .map(programRepository::save)
            .map(programMapper::toDto);
    }

    @Override
    public Page<ProgramDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Programs");
        return programRepository.findAll(pageable).map(programMapper::toDto);
    }

    @Override
    public Optional<ProgramDTO> findOne(String id) {
        log.debug("Request to get Program : {}", id);
        return programRepository.findById(id).map(programMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Program : {}", id);
        programRepository.deleteById(id);
    }
}
