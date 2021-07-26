package com.github.jadepeng.pipeline.service.impl;

import com.github.jadepeng.pipeline.domain.App;
import com.github.jadepeng.pipeline.repository.AppRepository;
import com.github.jadepeng.pipeline.service.AppService;
import com.github.jadepeng.pipeline.service.dto.AppDTO;
import com.github.jadepeng.pipeline.service.mapper.AppMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link App}.
 */
@Service
public class AppServiceImpl implements AppService {

    private final Logger log = LoggerFactory.getLogger(AppServiceImpl.class);

    private final AppRepository appRepository;

    private final AppMapper appMapper;

    public AppServiceImpl(AppRepository appRepository, AppMapper appMapper) {
        this.appRepository = appRepository;
        this.appMapper = appMapper;
    }

    @Override
    public AppDTO save(AppDTO appDTO) {
        log.debug("Request to save App : {}", appDTO);
        App app = appMapper.toEntity(appDTO);
        app = appRepository.save(app);
        return appMapper.toDto(app);
    }

    @Override
    public Optional<AppDTO> partialUpdate(AppDTO appDTO) {
        log.debug("Request to partially update App : {}", appDTO);

        return appRepository
            .findById(appDTO.getId())
            .map(
                existingApp -> {
                    appMapper.partialUpdate(existingApp, appDTO);

                    return existingApp;
                }
            )
            .map(appRepository::save)
            .map(appMapper::toDto);
    }

    @Override
    public Page<AppDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Apps");
        return appRepository.findAll(pageable).map(appMapper::toDto);
    }

    @Override
    public Optional<AppDTO> findOne(String id) {
        log.debug("Request to get App : {}", id);
        return appRepository.findById(id).map(appMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete App : {}", id);
        appRepository.deleteById(id);
    }
}
