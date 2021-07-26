package com.github.jadepeng.pipeline.service.impl;

import com.github.jadepeng.pipeline.domain.DockerImage;
import com.github.jadepeng.pipeline.repository.DockerImageRepository;
import com.github.jadepeng.pipeline.service.DockerImageService;
import com.github.jadepeng.pipeline.service.dto.DockerImageDTO;
import com.github.jadepeng.pipeline.service.mapper.DockerImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link DockerImage}.
 */
@Service
public class DockerImageServiceImpl implements DockerImageService {

    private final Logger log = LoggerFactory.getLogger(DockerImageServiceImpl.class);

    private final DockerImageRepository dockerImageRepository;

    private final DockerImageMapper dockerImageMapper;

    public DockerImageServiceImpl(DockerImageRepository dockerImageRepository, DockerImageMapper dockerImageMapper) {
        this.dockerImageRepository = dockerImageRepository;
        this.dockerImageMapper = dockerImageMapper;
    }

    @Override
    public DockerImageDTO save(DockerImageDTO dockerImageDTO) {
        log.debug("Request to save DockerImage : {}", dockerImageDTO);
        DockerImage dockerImage = dockerImageMapper.toEntity(dockerImageDTO);
        dockerImage = dockerImageRepository.save(dockerImage);
        return dockerImageMapper.toDto(dockerImage);
    }

    @Override
    public Optional<DockerImageDTO> partialUpdate(DockerImageDTO dockerImageDTO) {
        log.debug("Request to partially update DockerImage : {}", dockerImageDTO);

        return dockerImageRepository
            .findById(dockerImageDTO.getId())
            .map(
                existingDockerImage -> {
                    dockerImageMapper.partialUpdate(existingDockerImage, dockerImageDTO);

                    return existingDockerImage;
                }
            )
            .map(dockerImageRepository::save)
            .map(dockerImageMapper::toDto);
    }

    @Override
    public Page<DockerImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DockerImages");
        return dockerImageRepository.findAll(pageable).map(dockerImageMapper::toDto);
    }

    @Override
    public Optional<DockerImageDTO> findOne(String id) {
        log.debug("Request to get DockerImage : {}", id);
        return dockerImageRepository.findById(id).map(dockerImageMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete DockerImage : {}", id);
        dockerImageRepository.deleteById(id);
    }
}
