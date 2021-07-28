import Request from '@utils/request';

class PipelineCore {
    constructor () {
        this.module = '/job/api';
    }

    // 获取脚本列表
    getAllDockerImages (params = {}) {
        return Request.get(`${this.module}/docker-images`, {
            params,
        });
    }
}

export default new PipelineCore();
