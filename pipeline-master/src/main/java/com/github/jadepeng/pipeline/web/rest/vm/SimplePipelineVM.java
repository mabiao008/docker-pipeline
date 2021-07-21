package com.github.jadepeng.pipeline.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * View Model object for storing a user's credentials.
 */
@Data
public class SimplePipelineVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @Size(min = 1, max = 200)
    private String image;
}
