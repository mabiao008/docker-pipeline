package com.github.jadepeng.pipeline.service.impl;

import com.github.jadepeng.pipeline.domain.ProgramArguments;
import com.github.jadepeng.pipeline.repository.ProgramArgumentsRepository;
import com.github.jadepeng.pipeline.service.ProgramArgumentsService;
import com.github.jadepeng.pipeline.service.dto.ProgramArgumentsDTO;
import com.github.jadepeng.pipeline.service.mapper.ProgramArgumentsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ProgramArguments}.
 */
@Service
public class ProgramArgumentsServiceImpl implements ProgramArgumentsService {

    private final Logger log = LoggerFactory.getLogger(ProgramArgumentsServiceImpl.class);

    private final ProgramArgumentsRepository programArgumentsRepository;

    private final ProgramArgumentsMapper programArgumentsMapper;

    public ProgramArgumentsServiceImpl(
        ProgramArgumentsRepository programArgumentsRepository,
        ProgramArgumentsMapper programArgumentsMapper
    ) {
        this.programArgumentsRepository = programArgumentsRepository;
        this.programArgumentsMapper = programArgumentsMapper;
    }

    @Override
    public ProgramArgumentsDTO save(ProgramArgumentsDTO programArgumentsDTO) {
        log.debug("Request to save ProgramArguments : {}", programArgumentsDTO);
        ProgramArguments programArguments = programArgumentsMapper.toEntity(programArgumentsDTO);
        programArguments = programArgumentsRepository.save(programArguments);
        return programArgumentsMapper.toDto(programArguments);
    }

    @Override
    public Optional<ProgramArgumentsDTO> partialUpdate(ProgramArgumentsDTO programArgumentsDTO) {
        log.debug("Request to partially update ProgramArguments : {}", programArgumentsDTO);

        return programArgumentsRepository
            .findById(programArgumentsDTO.getId())
            .map(
                existingProgramArguments -> {
                    programArgumentsMapper.partialUpdate(existingProgramArguments, programArgumentsDTO);

                    return existingProgramArguments;
                }
            )
            .map(programArgumentsRepository::save)
            .map(programArgumentsMapper::toDto);
    }

    @Override
    public Page<ProgramArgumentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProgramArguments");
        return programArgumentsRepository.findAll(pageable).map(programArgumentsMapper::toDto);
    }

    @Override
    public Optional<ProgramArgumentsDTO> findOne(String id) {
        log.debug("Request to get ProgramArguments : {}", id);
        return programArgumentsRepository.findById(id).map(programArgumentsMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ProgramArguments : {}", id);
        programArgumentsRepository.deleteById(id);
    }
}
