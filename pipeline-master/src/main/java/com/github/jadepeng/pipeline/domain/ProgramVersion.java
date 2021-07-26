package com.github.jadepeng.pipeline.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 应用程序版本
 */
@Document(collection = "program_version")
public class ProgramVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 名称
     */
    @Field("name")
    private String name;

    /**
     * description
     */
    @Field("description")
    private String description;

    /**
     * 内容
     */
    @Field("content")
    private String content;

    /**
     * 文件路径
     */
    @Field("path")
    private String path;

    /**
     * 启动命令
     */
    @Field("command")
    private String command;

    /**
     * 版本
     */
    @Field("version")
    private String version;

    /**
     * 创建时间
     */
    @Field("created_date")
    private Instant createdDate;

    /**
     * 创建者
     */
    @Field("created_by")
    private String createdBy;

    @DBRef
    @Field("dockerImage")
    private DockerImage dockerImage;

    @DBRef
    @Field("arguments")
    @JsonIgnoreProperties(value = { "program" }, allowSetters = true)
    private Set<ProgramArguments> arguments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProgramVersion id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ProgramVersion name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProgramVersion description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return this.content;
    }

    public ProgramVersion content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return this.path;
    }

    public ProgramVersion path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCommand() {
        return this.command;
    }

    public ProgramVersion command(String command) {
        this.command = command;
        return this;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getVersion() {
        return this.version;
    }

    public ProgramVersion version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ProgramVersion createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ProgramVersion createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DockerImage getDockerImage() {
        return this.dockerImage;
    }

    public ProgramVersion dockerImage(DockerImage dockerImage) {
        this.setDockerImage(dockerImage);
        return this;
    }

    public void setDockerImage(DockerImage dockerImage) {
        this.dockerImage = dockerImage;
    }

    public Set<ProgramArguments> getArguments() {
        return this.arguments;
    }

    public ProgramVersion arguments(Set<ProgramArguments> programArguments) {
        this.setArguments(programArguments);
        return this;
    }

    public ProgramVersion addArguments(ProgramArguments programArguments) {
        this.arguments.add(programArguments);
        programArguments.setProgram(this);
        return this;
    }

    public ProgramVersion removeArguments(ProgramArguments programArguments) {
        this.arguments.remove(programArguments);
        programArguments.setProgram(null);
        return this;
    }

    public void setArguments(Set<ProgramArguments> programArguments) {
        if (this.arguments != null) {
            this.arguments.forEach(i -> i.setProgram(null));
        }
        if (programArguments != null) {
            programArguments.forEach(i -> i.setProgram(this));
        }
        this.arguments = programArguments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgramVersion)) {
            return false;
        }
        return id != null && id.equals(((ProgramVersion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramVersion{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", content='" + getContent() + "'" +
            ", path='" + getPath() + "'" +
            ", command='" + getCommand() + "'" +
            ", version='" + getVersion() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
