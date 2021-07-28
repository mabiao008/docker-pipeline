package com.github.jadepeng.pipeline.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.jadepeng.pipeline.domain.Program;

/**
 * A DTO for the {@link com.github.jadepeng.pipeline.domain.ProgramVersion} entity.
 */
@ApiModel(description = "应用程序版本")
public class ProgramVersionDTO implements Serializable {

    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * description
     */
    @ApiModelProperty(value = "description")
    private String description;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String path;

    /**
     * 启动命令
     */
    @ApiModelProperty(value = "启动命令")
    private String command;

    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Instant createdDate;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createdBy;

    private DockerImageDTO dockerImage;

    private List<TagDTO> tags;

    private ProgramDTO program;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DockerImageDTO getDockerImage() {
        return dockerImage;
    }

    public void setDockerImage(DockerImageDTO dockerImage) {
        this.dockerImage = dockerImage;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(
        List<TagDTO> tags) {
        this.tags = tags;
    }

    public ProgramDTO getProgram() {
        return program;
    }

    public void setProgram(ProgramDTO program) {
        this.program = program;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgramVersionDTO)) {
            return false;
        }

        ProgramVersionDTO programVersionDTO = (ProgramVersionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, programVersionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramVersionDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", content='" + getContent() + "'" +
            ", path='" + getPath() + "'" +
            ", command='" + getCommand() + "'" +
            ", version='" + getVersion() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", dockerImage=" + getDockerImage() +
            "}";
    }
}
