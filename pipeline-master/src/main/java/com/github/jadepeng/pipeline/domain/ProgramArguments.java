package com.github.jadepeng.pipeline.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 应用程序运行参数
 */
@Document(collection = "program_arguments")
public class ProgramArguments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 名称
     */
    @Field("name")
    private String name;

    /**
     * 提示信息
     */
    @Field("tooltip")
    private String tooltip;

    /**
     * 表单类型
     */
    @Field("form_type")
    private String formType;

    /**
     * 默认值
     */
    @Field("default_value")
    private String defaultValue;

    /**
     * placeholder
     */
    @Field("placeholder")
    private String placeholder;

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
    @Field("program")
    @JsonIgnoreProperties(value = { "dockerImage", "arguments" }, allowSetters = true)
    private ProgramVersion program;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProgramArguments id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ProgramArguments name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public ProgramArguments tooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getFormType() {
        return this.formType;
    }

    public ProgramArguments formType(String formType) {
        this.formType = formType;
        return this;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public ProgramArguments defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public ProgramArguments placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ProgramArguments createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ProgramArguments createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ProgramVersion getProgram() {
        return this.program;
    }

    public ProgramArguments program(ProgramVersion programVersion) {
        this.setProgram(programVersion);
        return this;
    }

    public void setProgram(ProgramVersion programVersion) {
        this.program = programVersion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgramArguments)) {
            return false;
        }
        return id != null && id.equals(((ProgramArguments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramArguments{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tooltip='" + getTooltip() + "'" +
            ", formType='" + getFormType() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", placeholder='" + getPlaceholder() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
