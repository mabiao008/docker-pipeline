package com.github.jadepeng.pipeline.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.jadepeng.pipeline.core.bean.Pipeline;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.PipelineJobTaskLog;
import com.github.jadepeng.pipeline.core.dto.JobState;
import com.github.jadepeng.pipeline.core.dto.PageDataResponse;
import com.github.jadepeng.pipeline.core.dto.PipelineRequest;
import com.github.jadepeng.pipeline.core.dto.RetCode;
import com.github.jadepeng.pipeline.repository.PipelineJobRepository;
import com.github.jadepeng.pipeline.repository.PipelineJobTaskLogRepository;
import com.github.jadepeng.pipeline.repository.PipelineRepository;
import com.github.jadepeng.pipeline.utils.DateUtil;
import com.github.jadepeng.pipeline.utils.StrUtil;
import com.github.jadepeng.pipeline.utils.UUIDUtils;


@Service
public class PipelineService {

    private final Logger log = LoggerFactory.getLogger(PipelineService.class);

    private final PipelineRepository pipelineRepository;
    private final PipelineJobRepository pipelineJobRepository;
    private final PipelineJobTaskLogRepository logRepository;
    private final MongoTemplate mongoTemplate;
    private final JobService jobService;

    @Autowired
    public PipelineService(PipelineRepository pipelineRepository,
                           PipelineJobRepository pipelineJobRepository,
                           PipelineJobTaskLogRepository logRepository,
                           MongoTemplate mongoTemplate,
                           JobService jobService) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineJobRepository = pipelineJobRepository;
        this.logRepository = logRepository;
        this.mongoTemplate = mongoTemplate;
        this.jobService = jobService;
    }

    public Pipeline createSimplePipeline(String name, String image) {
        Pipeline pipeline = Pipeline.buildDefaultJob(name, image);
        this.pipelineRepository.save(pipeline);
        return pipeline;
    }


    /**
     * Save a pipeline.
     *
     * @param pipeline the entity to save
     * @return the persisted entity
     */
    public Pipeline createPipeline(Pipeline pipeline) {
        log.debug("Request to create pipeline : {}", pipeline);
        // 校验name是否重复
        pipeline.setIsAvailable(1);
        List<Pipeline> duplicates =
            pipelineRepository.findAllByNameAndIsAvailable(pipeline.getName(),
                                                           pipeline
                                                               .getIsAvailable());
        if (0 < duplicates.size()) {
            throw new PipelineException(RetCode.PIPELINE_CANT_DUPLICATED);
        }
        pipeline.setId(UUIDUtils.getUUIDString());
        return pipelineRepository.save(pipeline);
    }

    /**
     * Update a pipeline
     *
     * @param pipeline the entity to update pipeline
     * @return the persisted entity
     */
    public Pipeline updatePipeline(Pipeline pipeline) {
        log.debug("Request to update pipeline : {}", pipeline);
        Optional<Pipeline> optional =
            pipelineRepository.findById(pipeline.getId());
        if (!optional.isPresent()) {
            throw new PipelineException(RetCode.INVALID_PIPLINE_ID);
        }
        Pipeline pipelineNew = optional.get();
        String newName = pipeline.getName();
        // 同一个图谱实例下的工作流名称不能重复
        List<Pipeline> duplicates =
            pipelineRepository.findAllByNameAndIsAvailable(newName, 1);
        if (1 < duplicates.size()) {
            throw new PipelineException(RetCode.PIPELINE_CANT_DUPLICATED);
        } else if (duplicates.size() == 1 &&
                   !duplicates.get(0).getId().equals(pipeline.getId())) {
            throw new PipelineException(RetCode.PIPELINE_CANT_DUPLICATED);
        }
        pipelineNew.setName(pipeline.getName());
        return pipelineRepository.save(pipelineNew);
    }

    public PageDataResponse<Pipeline> getAllPipelines(PipelineRequest request) {
        log.debug("Request to get all Pipelines");
        String name = request.getName();

        Pageable pageable = PageRequest
            .of(request.getPageIndex() - 1, request.getPageSize());

        Page<Pipeline> page = null;
        if (StringUtils.isBlank(name)) {
            page = pipelineRepository
                .findAllByIsAvailableOrderByCreatedDateDesc(1, pageable);
        } else {
            page = pipelineRepository
                .findAllByNameLikeAndIsAvailableOrderByCreatedDateDesc(name, 1,
                                                                       pageable);
        }

        return new PageDataResponse<>(page);
    }

    public JSONObject getAllPipelinesByDayNum() {
        // 统计 日
        JSONObject obj = new JSONObject();
        LocalDateTime start = DateUtil.getBeforeNDaysDateTime(30);
        LocalDateTime end = DateUtil.currentDayLastDateTime();
        List<Pipeline> monthPipelines = pipelineRepository
            .findAllByCreatedDateBetweenAndIsAvailableOrderByCreatedDateAsc(
                start, end, 1);
        Map<String, Integer> dateMap = new TreeMap<String, Integer>();
        int monthNum = 0;
        for (Pipeline pipeline : monthPipelines) {
            String dateStr = DateUtil
                .long2dateString(pipeline.getCreatedDate().getEpochSecond());
            if (dateMap.containsKey(dateStr)) {
                dateMap.put(dateStr, dateMap.get(dateStr) + 1);
            } else {
                dateMap.put(dateStr, 1);
            }
            monthNum += 1;
        }

        List<String> days = DateUtil.getBeforeNDays(30);
        for (String dateStr : days) {
            if (!dateMap.containsKey(dateStr)) {
                dateMap.put(dateStr, 0);
            }

        }

        // 构造month字段数据
        JSONObject monthObj = new JSONObject();
        monthObj.put("info", StrUtil.map2list(dateMap));
        monthObj.put("num", monthNum);
        obj.put("month", monthObj);

        List<String> weekDays = DateUtil.getBeforeNDays(7);
        Map<String, Integer> weekMap = new TreeMap<String, Integer>();
        int weekNum = 0;
        for (String dateStr : weekDays) {
            weekMap.put(dateStr, dateMap.get(dateStr));
            weekNum += dateMap.get(dateStr);
        }
        // 构造week字段数据
        JSONObject weekObj = new JSONObject();
        weekObj.put("info", StrUtil.map2list(weekMap));
        weekObj.put("num", weekNum);
        obj.put("week", weekObj);

        // 统计 月
        start = DateUtil.getBeforeNMonthsDateTime(12);
        List<Pipeline> yearPipelines = pipelineRepository
            .findAllByCreatedDateBetweenAndIsAvailableOrderByCreatedDateAsc(
                start, end, 1);
        Map<String, Integer> yearMap = new TreeMap<String, Integer>();
        int yearNum = 0;
        for (Pipeline pipeline : yearPipelines) {
            String monthStr = DateUtil
                .long2MonthString(pipeline.getCreatedDate().getEpochSecond());
            if (yearMap.containsKey(monthStr)) {
                yearMap.put(monthStr, yearMap.get(monthStr) + 1);
            } else {
                yearMap.put(monthStr, 1);
            }
            yearNum += 1;
        }

        List<String> months = DateUtil.getBeforeNMonths(12);
        for (String monthStr : months) {
            if (!yearMap.containsKey(monthStr)) {
                yearMap.put(monthStr, 0);
            }
        }
        // 构造year字段数据
        JSONObject yearObj = new JSONObject();
        yearObj.put("info", StrUtil.map2list(yearMap));
        yearObj.put("num", yearNum);
        obj.put("year", yearObj);

        return obj;

    }

    /**
     * Get one pipeline by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Pipeline getPipeline(String id) {
        log.debug("Request to get Pipeline : {}", id);
        Optional<Pipeline> optional = pipelineRepository.findById(id);
        if (!optional.isPresent()) {
            throw new PipelineException(RetCode.INVALID_PIPLINE_ID);
        }
        return optional.get();
    }


    /**
     * 复制pipeline
     *
     * @param pipelineId 工作流ID
     * @return
     */
    public Pipeline copyPipeline(String pipelineId) {
        Optional<Pipeline> pipelineOptional =
            pipelineRepository.findById(pipelineId);
        if (!pipelineOptional.isPresent()) {
            throw new PipelineException(RetCode.INVALID_PIPLINE_ID);
        }
        Pipeline pipeline = pipelineOptional.get();

        //工作流名称
        List<Pipeline> pipelines = pipelineRepository
            .findAllByNameLikeAndIsAvailable(pipeline.getName() + " - 副本", 1);
        if (pipelines.isEmpty()) {
            pipeline.setName(pipeline.getName() + " - 副本");
        } else {
            pipeline.setName(
                pipeline.getName() + " - 副本" + "(" + pipelines.size() + ")");
        }

        if (pipeline != null) {
            pipeline.setId(UUIDUtils.getUUIDString());
            pipeline.setCreatedDate(Instant.now());
            pipelineRepository.save(pipeline);
            return pipeline;
        }

        return null;
    }

    public PipelineJob runPipeline(String pipelineId) {

        Pipeline pipeline = this.pipelineRepository.findById(pipelineId)
                                                   .orElseThrow(
                                                       () -> new PipelineException(
                                                           RetCode.INVALID_PIPLINE_ID));
        if (pipeline.getIsAvailable() == 0) {
            throw new PipelineException(RetCode.PIPELINE_DELETEED);
        }

        return this.jobService.runPipeline(pipeline);
    }

    public boolean cancelPipelineJob(String pipelineJobId) {
        PipelineJob job = this.getJob(pipelineJobId);
        return this.jobService.cancelJob(job);
    }

    /**
     * 获取最新的job
     *
     * @param pipelineId
     * @return
     */
    public PipelineJob getLastPipelineJob(String pipelineId) {
        PipelineJob pipelineJob = new PipelineJob();
        Query query = new Query().addCriteria(
            Criteria.where("pipeline.$id").is(pipelineId))
                                 .with(Sort.by(Sort.Direction.DESC,
                                               "createdDate"))
                                 .limit(1);
        List<PipelineJob> pipelineJobs =
            mongoTemplate.find(query, PipelineJob.class);
        if (pipelineJobs.size() > 0) {
            return pipelineJobs.get(0);
        }

        return pipelineJob;
    }

    /**
     * Delete the pipeline by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        // TODO check right
        log.debug("Request to deletePipelineTask Pipeline : {}", id);
        Optional<Pipeline> optional = pipelineRepository.findById(id);
        if (!optional.isPresent()) {
            throw new PipelineException(RetCode.INVALID_PIPLINE_ID);
        }
        Pipeline pipeline = optional.get();
        pipeline.setIsAvailable(0);
        pipelineRepository.save(pipeline);
    }

    public void changeJobState(JobState state) {
        PipelineJob job = this.getJob(state.getJobId());
        job.setStatus(state.getStatus());

        switch (state.getStatus()) {
            case RUNNING:
                job.setStartTime(state.getTimestamp());
                break;
            case FINISHED:
            case FAIL:
                job.setFinishTime(state.getTimestamp());
                break;
        }
        job.setExitedValue(state.getExitedValue());
        job.setCurrentTask(state.getCurrentTask());

        this.pipelineJobRepository.save(job);
    }

    private PipelineJob getJob(String jobId) {
        return this.pipelineJobRepository
                    .findById(jobId)
                    .orElseThrow(() -> new PipelineException(RetCode.INVALID_PIPELINE_JOB_ID));
    }

    public void saveJobTaskLog(PipelineJobTaskLog log) {
        this.logRepository.save(log);
    }

    public PipelineJob getPipelineJob(String jobId) {
        return this.pipelineJobRepository.findById(jobId).orElse(null);
    }
}
