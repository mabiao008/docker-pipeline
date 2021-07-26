package com.github.jadepeng.pipeline.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.github.jadepeng.pipeline.domain.ProgramArguments} entity.
 */
@ApiModel(description = "应用程序运行参数")
public class ProgramArgumentsDTO implements Serializable {

    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 提示信息
     */
    @ApiModelProperty(value = "提示信息")
    private String tooltip;

    /**
     * 表单类型
     */
    @ApiModelProperty(value = "表单类型")
    private String formType;

    /**
     * 默认值
     */
    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    /**
     * placeholder
     */
    @ApiModelProperty(value = "placeholder")
    private String placeholder;

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

    private ProgramVersionDTO program;

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

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
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

    public ProgramVersionDTO getProgram() {
        return program;
    }

    public void setProgram(ProgramVersionDTO program) {
        this.program = program;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgramArgumentsDTO)) {
            return false;
        }

        ProgramArgumentsDTO programArgumentsDTO = (ProgramArgumentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, programArgumentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramArgumentsDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", tooltip='" + getTooltip() + "'" +
            ", formType='" + getFormType() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", placeholder='" + getPlaceholder() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", program=" + getProgram() +
            "}";
    }
}
