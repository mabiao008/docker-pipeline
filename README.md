# pipeline 

基于docker的分布式任务执行器（研发中）

- pipeline master 中心节点，管理和调度任务
- pipeline agent 执行任务的节点，接收到任务后，调用docker执行pipeline任务

## 架构

![架构](https://gitee.com/jadepeng/pic/raw/master/pic/2021/7/21/1626870685756.png)

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
- [x] rolling日志接口
- [x] 运行老版本pipeline任务
- [ ] crontab 定时执行
- [ ] 快速创建任务，支持python、node等脚本程序直接执行
- [ ] 根据资源配额（内存、CPU）调度任务, 运行任务需要指定资源配额
- [ ] agent 增加label标识，调度时可以调度到指定label的agent，比如gpu=true
- [ ] 增加任务管理web, 管理提交任务、查询运行日志等

## 进展

### 2021.07.28
- 新增运行老版本pipeline任务能力
- 增加日志接口

### 2021.07.27

- 引入bk-job的ui，待修改

### 2021.07.21 

- Master 调用 agent执行任务
- agnet 启动docker执行任务

### 2021.07.19

- 基于jhipster搭建框架
- 分布式实现


## 使用说明

### 安装部署

#### 编译

使用mvn编译

```bash
mvn package -DskipTests
```

#### 部署master

根据需要，修改master的prod配置文件`application-prod.yml`。

包含`kafka`配置，`server`端口，`mongodb`地址,`jwt secret`配置。

mongodb 会自动新建collection和初始化数据，无需手动导入数据。

```yaml
kafka:
    producer:
      bootstrap-servers: 172.31.161.38:9092
      retries: 3
      batch-size: 2000
      buffer-memory: 33554432
    consumer:
      group-id: consumer-pipeline
      auto-offset-reset: earliest
      enable-auto-commit: true
      bootstrap-servers: 172.31.161.38:9092

server:
  port: 8080

spring:
  data:
    mongodb:
      uri: mongodb://172.31.161.38:28017
      database: pipeline

jhipster:
  security:
    authentication:
      jwt:
        base64-secret:
```

注意master的jwt secret需要和agent的保持一致。

配置好后，启动：

```bash
nohup java -jar pipeline-master-$version.jar --spring.profiles.active=prod &
```

可以将`application-prod.yml`放在和jar包同一目录。

#### 部署agent

根据需要，修改master的prod配置文件`application-prod.yml`。

包含：

- eureka的defaultZone，配置master的地址
- 端口
- docker地址
    - docker-tls-verify: 是否启动tls验证
    - docker-cert-path：启动tls验证的ca证书
    - pipeline-log-path: 运行日志存储路径

```yaml

eureka:
  instance:
    prefer-ip-address: true
  client:
    service-url:
      defaultZone: http://admin:${jhipster.registry.password}@127.0.0.1:8080/eureka/

server:
  port: 8081

application:
  docker-server: 172.31.161.38
  docker-tls-verify: true
  docker-cert-path: /mnt/parastor/pipeline/ca/
  pipeline-log-path: /mnt/parastor/pipeline/logs/


jhipster:
  security:
    authentication:
      jwt:
        base64-secret:
```

### 执行老版本任务

    POST /api/pipelines/exec-old


    Body：
    
```json
{
	"networks":[
		{
			"driver":"bridge",
			"name":"pipeline_network_3eac4b36209a41e58a5f22dd403fee50"
		}
	],
	"pipeline":[
		{
			"alias":"Word",
			"dependencies":[],
			"name":"pipeline_task_3eac4b36209a41e58a5f22dd403fee50_1",
			"nextPipelines":[],
			"steps":[
				{
					"alias":"Word",
					"auth_config":{},
					"command":[
						"echo $CI_SCRIPT | base64 -d | /bin/bash -e"
					],
					"entrypoint":[
						"/bin/bash",
						"-c"
					],
					"environment":{
						"CI_SCRIPT":"CmlmIFsgLW4gIiRDSV9ORVRSQ19NQUNISU5FIiBdOyB0aGVuCmNhdCA8PEVPRiA+ICRIT01FLy5uZXRyYwptYWNoaW5lICRDSV9ORVRSQ19NQUNISU5FCmxvZ2luICRDSV9ORVRSQ19VU0VSTkFNRQpwYXNzd29yZCAkQ0lfTkVUUkNfUEFTU1dPUkQKRU9GCmNobW9kIDA2MDAgJEhPTUUvLm5ldHJjCmZpCnVuc2V0IENJX05FVFJDX1VTRVJOQU1FCnVuc2V0IENJX05FVFJDX1BBU1NXT1JECnVuc2V0IENJX1NDUklQVAplY2hvICsgamF2YSAtY3AgL2RhdGF2b2x1bWUvcGRmX3RvX3dvcmQvcGRmYm94X3V0aWwtMS4wLVNOQVBTSE9ULmphciBjb20uaWZseXRlay5pbmRleGVyLlJ1bm5lciAtLWlucHV0UERGIC9kYXRhdm9sdW1lL2V4dHJhY3QvZjkyYzJhNzViYWU4NGJiMDg4MzIwNWRiM2YyZGFlNzkvcGRmL2VjNWMwYjk0M2QwYjRmNDI5MzcyMmE1ZGRjNjFlNjZkL0hTNy5wZGYgLS1vdXRwdXRXb3JkIC9kYXRhdm9sdW1lL2V4dHJhY3QvZjkyYzJhNzViYWU4NGJiMDg4MzIwNWRiM2YyZGFlNzkvcGRmVG9Xb3JkL2VjNWMwYjk0M2QwYjRmNDI5MzcyMmE1ZGRjNjFlNjZkLyAtLXNjaGVtYUlucHV0UGF0aCAvZGF0YXZvbHVtZS9leHRyYWN0L2ticWEvZjkyYzJhNzViYWU4NGJiMDg4MzIwNWRiM2YyZGFlNzkgLS1lbnRpdHlJbmRleFBhdGggL2RhdGF2b2x1bWUvZXh0cmFjdC9mOTJjMmE3NWJhZTg0YmIwODgzMjA1ZGIzZjJkYWU3OS9wZGZUb1dvcmQvZW50aXR5IC0tZmllbGRJbmRleFBhdGggL2RhdGF2b2x1bWUvZXh0cmFjdC9mOTJjMmE3NWJhZTg0YmIwODgzMjA1ZGIzZjJkYWU3OS9wZGZUb1dvcmQvZmllbGQgLS10eXBlIGx1Y2VuZSAtLW91dHB1dCAvZGF0YXZvbHVtZS9leHRyYWN0L2Y5MmMyYTc1YmFlODRiYjA4ODMyMDVkYjNmMmRhZTc5L3BkZlRvV29yZC9lYzVjMGI5NDNkMGI0ZjQyOTM3MjJhNWRkYzYxZTY2ZC9lbnRpdHlJbmZvLnR4dApqYXZhIC1jcCAvZGF0YXZvbHVtZS9wZGZfdG9fd29yZC9wZGZib3hfdXRpbC0xLjAtU05BUFNIT1QuamFyIGNvbS5pZmx5dGVrLmluZGV4ZXIuUnVubmVyIC0taW5wdXRQREYgL2RhdGF2b2x1bWUvZXh0cmFjdC9mOTJjMmE3NWJhZTg0YmIwODgzMjA1ZGIzZjJkYWU3OS9wZGYvZWM1YzBiOTQzZDBiNGY0MjkzNzIyYTVkZGM2MWU2NmQvSFM3LnBkZiAtLW91dHB1dFdvcmQgL2RhdGF2b2x1bWUvZXh0cmFjdC9mOTJjMmE3NWJhZTg0YmIwODgzMjA1ZGIzZjJkYWU3OS9wZGZUb1dvcmQvZWM1YzBiOTQzZDBiNGY0MjkzNzIyYTVkZGM2MWU2NmQvIC0tc2NoZW1hSW5wdXRQYXRoIC9kYXRhdm9sdW1lL2V4dHJhY3Qva2JxYS9mOTJjMmE3NWJhZTg0YmIwODgzMjA1ZGIzZjJkYWU3OSAtLWVudGl0eUluZGV4UGF0aCAvZGF0YXZvbHVtZS9leHRyYWN0L2Y5MmMyYTc1YmFlODRiYjA4ODMyMDVkYjNmMmRhZTc5L3BkZlRvV29yZC9lbnRpdHkgLS1maWVsZEluZGV4UGF0aCAvZGF0YXZvbHVtZS9leHRyYWN0L2Y5MmMyYTc1YmFlODRiYjA4ODMyMDVkYjNmMmRhZTc5L3BkZlRvV29yZC9maWVsZCAtLXR5cGUgbHVjZW5lIC0tb3V0cHV0IC9kYXRhdm9sdW1lL2V4dHJhY3QvZjkyYzJhNzViYWU4NGJiMDg4MzIwNWRiM2YyZGFlNzkvcGRmVG9Xb3JkL2VjNWMwYjk0M2QwYjRmNDI5MzcyMmE1ZGRjNjFlNjZkL2VudGl0eUluZm8udHh0Cg=="
					},
					"image":"registry.iflyresearch.com/aimind/java:v1.0.0",
					"name":"pipeline_task_3eac4b36209a41e58a5f22dd403fee50_1",
					"networks":[
						{
							"aliases":[
								"default"
							],
							"name":"pipeline_network_3eac4b36209a41e58a5f22dd403fee50"
						}
					],
					"on_success":true,
					"volumes":[
						"pipeline_default:/aimind",
						"/mnt/parastor/aimind/shared/:/share",
						"/mnt/parastor/aimind/pipeline-jobs/2021/07/26/3eac4b36209a41e58a5f22dd403fee50:/workspace",
						"/mnt/parastor/aimind/datavolumes/carmaster:/datavolume"
					],
					"working_dir":"/workspace"
				}
			]
		}
	],
	"volumes":[
		{
			"driver":"local",
			"name":"pipeline_default"
		}
	]
}
```

成功返回：

```json
{
    "retcode": "000000",
    "desc": "成功",
    "data": {
        "id": "8137f344-f52d-4595-bdbb-425363847b61",
    }
}
```

可根据id获取日志。

### 获取job执行日志

    GET /api/pipelines/jobLog/{jobid}/

结果：

```json
{
    "retcode": "000000",
    "desc": "成功",
    "data": {
        "currentTask": null,
        "logs": [
            {
                "id": "e76a686f68b64c0783b7721b058be137",
                "jobId": "8137f344-f52d-4595-bdbb-425363847b61",
                "status": "FINISHED",
                "taskName": "pipeline_task_3eac4b36209a41e58a5f22dd403fee50_1",
                "exitedValue": 0,
                "logs": [
                    "proc \"pipeline_task_3eac4b36209a41e58a5f22dd403fee50_1\" started",
                    "pipeline_task_3eac4b36209a41e58a5f22dd403fee50_1:+ java -cp /datavolume/pdf_to_word/pdfbox_util-1.0-SNAPSHOT.jar com.iflytek.indexer.Runner --inputPDF /datavolume/extract/f92c2a75bae84bb0883205db3f2dae79/pdf/ec5c0b943d0b4f4293722a5ddc61e66d/HS7.pdf --outputWord /datavolume/extract/f92c2a75bae84bb0883205db3f2dae79/pdfToWord/ec5c0b943d0b4f4293722a5ddc61e66d/ --schemaInputPath /datavolume/extract/kbqa/f92c2a75bae84bb0883205db3f2dae79 --entityIndexPath /datavolume/extract/f92c2a75bae84bb0883205db3f2dae79/pdfToWord/entity --fieldIndexPath /datavolume/extract/f92c2a75bae84bb0883205db3f2dae79/pdfToWord/field --type lucene --output /datavolume/extract/f92c2a75bae84bb0883205db3f2dae79/pdfToWord/ec5c0b943d0b4f4293722a5ddc61e66d/entityInfo.txt",
                    "proc \"pipeline_task_3eac4b36209a41e58a5f22dd403fee50_1\" exited with status 0"
                ]
            }
        ],
        "exitedValue": 0,
        "status": "FINISHED",
        "pipelineJobSt": 1627477250599,
        "pipelineJobFt": 1627477274299
    }
}
```
