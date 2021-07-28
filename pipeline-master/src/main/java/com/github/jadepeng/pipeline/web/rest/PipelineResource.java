package com.github.jadepeng.pipeline.web.rest;

import java.net.URISyntaxException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.jadepeng.pipeline.core.api.MasterApi;
import com.github.jadepeng.pipeline.core.bean.Pipeline;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.PipelineJobLog;
import com.github.jadepeng.pipeline.core.bean.PipelineJobTaskLog;
import com.github.jadepeng.pipeline.core.dto.BasePayloadResponse;
import com.github.jadepeng.pipeline.core.dto.BaseResponse;
import com.github.jadepeng.pipeline.core.dto.JobState;
import com.github.jadepeng.pipeline.core.dto.PageDataResponse;
import com.github.jadepeng.pipeline.core.dto.PipelineRequest;
import com.github.jadepeng.pipeline.service.PipelineService;
import com.github.jadepeng.pipeline.utils.UUIDUtils;
import com.github.jadepeng.pipeline.web.rest.vm.PipelineJson;
import com.github.jadepeng.pipeline.web.rest.vm.SimplePipelineVM;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/pipelines")
public class PipelineResource implements MasterApi {

    private final Logger log = LoggerFactory.getLogger(PipelineResource.class);

    private final PipelineService pipelineService;

    public PipelineResource(
        PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    /**
     * POST  /pipelines : Create a new pipeline.
     *
     * @param pipeline the pipeline to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new pipeline, or with status 400 (Bad Request) if the pipeline has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/")
    @ResponseBody
    @ApiOperation(value = "工作流创建接口", httpMethod = "POST",
                  response = ResponseEntity.class)
    public BasePayloadResponse<Pipeline> createPipeline(
        @NotNull(message = "pipeline不能为空") @Valid @RequestBody
            Pipeline pipeline) throws URISyntaxException {
        log.debug("REST request to createPipeline Pipeline : {}", pipeline);
        return BasePayloadResponse
            .success(pipelineService.createPipeline(pipeline));
    }

    @PostMapping("/image")
    @ResponseBody
    @ApiOperation(value = "工作流创建接口", httpMethod = "POST",
                  response = ResponseEntity.class)
    public BasePayloadResponse<Pipeline> createImagePipeline(
        @NotNull(message = "pipeline不能为空") @Valid @RequestBody
            SimplePipelineVM pipeline) throws URISyntaxException {
        log.debug("REST request to createPipeline Pipeline : {}", pipeline);
        return BasePayloadResponse
            .success(pipelineService.createSimplePipeline(pipeline.getName(),
                                                          pipeline.getImage()));
    }

    /**
     * PUT  /pipelines : Updates an existing pipeline.
     *
     * @param pipeline the pipeline to update
     * @return the ResponseEntity with status 200 (OK) and with body the
     * updated pipeline,
     * or with status 400 (Bad Request) if the pipeline is not valid,
     * or with status 500 (Internal Server Error) if the pipeline couldn't be
     * updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/")
    @ResponseBody
    @ApiOperation(value = "工作流编辑接口", httpMethod = "PUT",
                  response = BasePayloadResponse.class)
    public BasePayloadResponse<Pipeline> updatePipeline(
        @NotNull(message = "pipeline不能为空")
        @Valid @RequestBody Pipeline pipeline) throws URISyntaxException {
        log.debug("REST request to update Pipeline : {}", pipeline);
        return BasePayloadResponse
            .success(pipelineService.updatePipeline(pipeline));
    }

    /**
     * GET  /pipelines : get all the pipelines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * pipelines in body
     */
    @GetMapping("/")
    @ResponseBody
    @ApiOperation(value = "查询所有工作流接口", httpMethod = "GET",
                  response = BasePayloadResponse.class)
    public BasePayloadResponse<PageDataResponse<Pipeline>> getAllPipelines(
        @Valid PipelineRequest request) {
        log.debug("REST request to get all Pipelines");
        return BasePayloadResponse
            .success(pipelineService.getAllPipelines(request));
    }

    /**
     * GET  /pipelines : get all the pipelines num groupby day.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * pipelines num in body
     */
    @GetMapping("/num")
    @ResponseBody
    @ApiOperation(value = "查询所有工作流按天统计数据量接口", httpMethod = "GET",
                  response = JSONObject.class)
    public BasePayloadResponse<JSONObject> getAllPipelinesByDayNum() {
        return BasePayloadResponse
            .success(pipelineService.getAllPipelinesByDayNum());
    }

    /**
     * GET  /pipelines/:id : get the "id" pipeline.
     *
     * @param id the id of the pipeline to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * pipeline, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "查询工作流接口", httpMethod = "GET",
                  response = BasePayloadResponse.class)
    public BasePayloadResponse<Pipeline> getPipeline(@PathVariable String id) {
        log.debug("REST request to get Pipeline : {}", id);
        return BasePayloadResponse.success(pipelineService.getPipeline(id));
    }


    @GetMapping("/copy/{id}")
    @ResponseBody
    @ApiOperation(value = "复制工作流", httpMethod = "GET",
                  response = BasePayloadResponse.class)
    public BasePayloadResponse<Pipeline> copyPipeline(@PathVariable String id) {
        log.debug("REST request to get Pipeline : {}", id);
        return BasePayloadResponse.success(pipelineService.copyPipeline(id));
    }

    @GetMapping("/lastjob/{id}")
    @ResponseBody
    @ApiOperation(value = "获取最新的pipelinejob", httpMethod = "GET",
                  response = BasePayloadResponse.class)
    public BasePayloadResponse<PipelineJob> getLastPipelineJob(
        @PathVariable String id) {
        log.debug("REST request to get Pipeline : {}", id);
        return BasePayloadResponse
            .success(pipelineService.getLastPipelineJob(id));
    }

    @PostMapping("/exec-old")
    @ResponseBody
    @ApiOperation(value = "执行老pipelineJson", httpMethod = "POST",
                  response = ResponseEntity.class)
    public BasePayloadResponse<PipelineJob> executeOldPipeline(
        @NotNull(message = "pipeline不能为空") @Valid @RequestBody
            PipelineJson pipeline) throws URISyntaxException {
        log.debug("REST request to exec old Pipeline : {}", pipeline);
        Pipeline newPipeline = pipeline.toPipeline();
        newPipeline.setId(UUIDUtils.getUUIDString());
        return BasePayloadResponse
            .success(pipelineService.runPipeline(newPipeline));
    }

    @GetMapping("/exec/{id}")
    @ResponseBody
    @ApiOperation(value = "执行工作流", httpMethod = "GET")
    public BasePayloadResponse<PipelineJob> executePipeline(@PathVariable String id) {
        log.debug("REST request to get Pipeline : {}", id);
        return BasePayloadResponse.success(pipelineService.runPipeline(id));
    }

    @GetMapping("/cancel/{jobId}")
    @ResponseBody
    @ApiOperation(value = "取消工作流任务", httpMethod = "GET")
    public BasePayloadResponse<Boolean> cancelPipelineJob(@PathVariable String jobId) {
        log.debug("REST request to cancel pipeline job: {}", jobId);
        return BasePayloadResponse.success(pipelineService.cancelPipelineJob(jobId));
    }

    ///**
    // * GET  /pipelines/:id : get the "id" pipeline.
    // *
    // * @param pipelineJobId the id of the pipeline to retrieve
    // * @return the ResponseEntity with status 200 (OK) and with body the
    //pipeline, or with status 404 (Not Found)
    // */
    //@GetMapping("/pipelines/logs/{pipelineJobId}")
    //@ResponseBody
    //@ApiOperation(value = "查询工作流任务日志接口", httpMethod = "GET", response =
    //BasePayloadResponse.class)
    //public BasePayloadResponse<PipelineJobLog> getPipelineJobLogs
    //(@PathVariable String pipelineJobId) {
    //  log.debug("REST request to get Pipeline latest logs : {}",
    // pipelineJobId);
    //  return BasePayloadResponse.success(pipelineService.getPipelineJobLogs
    // (pipelineJobId));
    //}
    //
    //@GetMapping("/pipelines/stop/{pipelineId}")
    //@ResponseBody
    //@ApiOperation(value = "停止运行的工作流任务", httpMethod = "GET", response =
    //BasePayloadResponse.class)
    //public BasePayloadResponse<PipelineJob> stopPipelineJob(@PathVariable
    //String pipelineId) {
    //  log.debug("REST request to get Pipeline latest logs : {}", pipelineId);
    //  return BasePayloadResponse.success(pipelineService.stopPipelineJob
    // (pipelineId));
    //}
    //
    ///**
    // * 工作流日志查看接口
    // *
    // * @param pipelineJobsReq
    // * @return
    // */
    //@GetMapping("/pipelines/pipeline-jobs")
    //@ResponseBody
    //@ApiOperation(value = "查询工作流的所有任务接口", httpMethod = "GET", response =
    //BasePayloadResponse.class)
    //public BasePayloadResponse<PipelineJobsResp> getPipelineJobs(@Valid
    //PipelineJobsReq pipelineJobsReq) {
    //  log.debug("REST request to get Pipeline latest logs : {}",
    // pipelineJobsReq);
    //  return BasePayloadResponse.success(pipelineService.getPipelineJobs
    // (pipelineJobsReq));
    //}

    /**
     * DELETE  /pipelines/:id : deletePipelineTask the "id" pipeline.
     *
     * @param id the id of the pipeline to deletePipelineTask
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "工作流删除接口", httpMethod = "DELETE",
                  response = BasePayloadResponse.class)
    public BasePayloadResponse deletePipeline(@PathVariable String id) {
        log.debug("REST request to deletePipelineTask Pipeline : {}", id);
        pipelineService.delete(id);
        return BasePayloadResponse.success(id);
    }

    @Override
    public BaseResponse jobStateChange(JobState state) {
        this.pipelineService.changeJobState(state);
        return BasePayloadResponse.success(state.getJobId());
    }

    @Override
    public BaseResponse saveJobTaskLog(PipelineJobTaskLog log) {
        this.pipelineService.saveJobTaskLog(log);
        return BasePayloadResponse.success(log.getJobId());
    }

    @RequestMapping(value = "/jobLog/{jobId}", method = RequestMethod.GET)
    @Timed
    public BasePayloadResponse<PipelineJobLog> getJobLog(@PathVariable String jobId) {
        return BasePayloadResponse.success(this.pipelineService.getJobLog(jobId));
    }
}
