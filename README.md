# pipeline 

基于docker的分布式任务执行器（研发中）

- pipeline master 中心节点，管理和调度任务
- pipeline agent 执行任务的节点，接收到任务后，调用docker执行pipeline任务

## 任务定义

一个pipeline 任务：

- 支持多个pipelineTask
- 一个pipelineTask 包含多个Step


```json
{
        "id": "29103d5e4a77409b9f6050eea8110bb3",
        "name": "test",
        "pipelineTasks": [
            {
                "name": "docker image pipeline",
                "alias": null,
                "dependencies": null,
                "steps": [
                    {
                        "name": "defaultJob",
                        "alias": null,
                        "image": "java-pipeline:1.0.1",
                        "workingDir": "/workspace",
                        "environment": {},
                        "entryPoint": null,
                        "command": null,
                        "volumes": null,
                        "networks": [
                            {
                                "name": "pipeline_default",
                                "aliases": [
                                    "default"
                                ]
                            }
                        ],
                        "onSuccess": false,
                        "authConfig": {},
                        "environmentString": []
                    }
                ]
            }
        ],
        "networks": [
            {
                "name": "pipeline_default",
                "driver": "bridge"
            }
        ],
        "volumes": [
            {
                "name": "pipeline_default",
                "driver": "local"
            }
        ],
        "startNode": null,
        "runOnce": true,
        "crontab": null,
        "isAvailable": 1,
        "runningPipelines": [],
        "finishedPipeliens": [],
        "finished": false
    }

```
## 功能特性

- [x] 分布式框架，高可用，服务注册与状态维护
- [x] Agent执行任务
- [] rolling日志接口
- [] crontab 定时执行
- [] 增加快速创建任务，支持python、node等脚本程序直接执行
- [] 根据内存、CPU 调度
- [] agent 增加label，调度时可以调度到指定label的agent，比如gpu=true
- [] 增加任务管理web

## 进展

### 2021.07.21 

- Master 调用 agent执行任务
- agnet 启动docker执行任务

### 2021.07.19

- 基于jhipster搭建框架
- 分布式实现
