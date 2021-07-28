
/* eslint-disable no-param-reassign */
import PipelineSource from '../source/pipeline-core';
import Request from '@utils/request';

export default {
    
    /**
     * 加载docker images
     * @param {*} params 
     * @returns 
     */
    fetchDockerImages (params) {
        return PipelineSource.getAllDockerImages(params)
            .then(({ data }) => data);
    },

    getDataByScriptId ({ id }, payload = {}) {
        return Request.get(`/job/api/script/${id}`, {
            payload,
        }).then(({ data }) => data);
    }
};
