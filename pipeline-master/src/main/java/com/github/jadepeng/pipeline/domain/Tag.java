package com.github.jadepeng.pipeline.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 标签
 */
@Document(collection = "tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 名称
     */
    @Field("name")
    private String name;

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
    @Field("app")
    @JsonIgnoreProperties(value = { "tags" }, allowSetters = true)
    private App app;

    @DBRef
    @Field("program")
    @JsonIgnoreProperties(value = { "onlineVersion", "tags" }, allowSetters = true)
    private Program program;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tag id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Tag createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Tag createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public App getApp() {
        return this.app;
    }

    public Tag app(App app) {
        this.setApp(app);
        return this;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Program getProgram() {
        return this.program;
    }

    public Tag program(Program program) {
        this.setProgram(program);
        return this;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
