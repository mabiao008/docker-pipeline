/*
 * Tencent is pleased to support the open source community by making BK-JOB蓝鲸智云作业平台 available.
 *
 * Copyright (C) 2021 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-JOB蓝鲸智云作业平台 is licensed under the MIT License.
 *
 * License for BK-JOB蓝鲸智云作业平台:
 *
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
*/

import './public-path';
import Vue from 'vue';
import createRouter from '@/router';
import store from '@/store';
// import QueryGlobalSettingService from '@service/query-global-setting';
import TaskExecuteService from '@service/task-execute';
import TaskPlanService from '@service/task-plan';
import EntryTask from '@/utils/entry-task';
import { appIdCache } from '@/utils/cache-helper';
import '@/common/bkmagic';
import '@/css/icon-cool/iconcool.js';
import '@/css/reset.css';
import '@/css/app.css';
import App from '@/App';
import IframeApp from '@/iframe-app';
import i18n from '@/i18n';

/**
 * @desc 启动打印当前系统信息
 */
console.log(process.env.JOB_WELCOME);

/**
 * @desc 页面数据的编辑状态
 */
window.changeAlert = false;

/**
 * @desc 开启路由回溯
 */
window.routerFlashBack = false;

/**
 * @desc 浏览器框口关闭提醒
 */
window.addEventListener('beforeunload', (event) => {
    if (!window.changeAlert) {
        return null;
    }
    const e = event || window.event;
    if (e) {
        e.returnValue = window.BKApp.$t('离开将会导致未保存信息丢失');
    }
    return window.BKApp.$t('离开将会导致未保存信息丢失');
});

const entryTask = new EntryTask();

/**
 * @desc 根据环境动态判断使用那个入口
 *
 * 通过浏览器直接访问：App
 * 通过 iframe 访问任务详情：IframeApp
 */
let EntryApp = App;

/**
 * @desc 解析默认appid
 */
entryTask.add((context) => {
    const pathAppIdMatch = window.location.pathname.match(/^\/(\d+)\/?/);

    if (pathAppIdMatch) {
    // 路由指定了业务id
    // eslint-disable-next-line no-param-reassign
        context.appId = ~~pathAppIdMatch[1];
    } else {
    // 本地缓存
    // eslint-disable-next-line no-param-reassign
        context.appId = appIdCache.getItem();
    }
});

/**
 * @desc 通过第三方系统查看任务执行详情
 */
const apiExecute = window.location.href.match(/api_execute\/([^/]+)/);
if (apiExecute) {
    // 通过 iframe 访问任务详情入口为 IframeApp
    if (window.frames.length !== parent.frames.length) {
        EntryApp = IframeApp;
    }
    entryTask.add(
        context => TaskExecuteService.fetchTaskInstanceFromAllApp({
            taskInstanceId: apiExecute[1],
        }).then((data) => {
            // eslint-disable-next-line no-param-reassign
            context.taskData = data;
            // eslint-disable-next-line no-param-reassign
            context.appId = data.appId;
        }),
        (context) => {
            const { taskData } = context;
            if (taskData.isTask) {
                window.BKApp.$router.replace({
                    name: 'historyTask',
                    params: {
                        id: taskData.id,
                    },
                });
            } else {
                window.BKApp.$router.replace({
                    name: 'historyStep',
                    params: {
                        taskInstanceId: taskData.id,
                    },
                });
            }
        },
    );
}
/**
 * @desc 通过第三方系统查看执行方案详情
 */
const apiPlan = window.location.href.match(/api_plan\/([^/]+)/);
if (apiPlan) {
    entryTask.add(
        context => TaskPlanService.fetchPlanData({
            id: apiPlan[1],
        }).then((data) => {
            // eslint-disable-next-line no-param-reassign
            context.planData = data;
            // eslint-disable-next-line no-param-reassign
            context.appId = data.appId;
        }),
        (context) => {
            const { planData } = context;
            window.BKApp.$router.replace({
                name: 'viewPlan',
                params: {
                    templateId: planData.templateId,
                },
                query: {
                    viewPlanId: planData.id,
                },
            });
        },
    );
}

entryTask.add((context) => {
    context.appId = 'ainote';
    context.isAdmin = true;
    context.appList = ['ainote', 'aimind'];
});

/**
 * @desc 渲染页面
 */
entryTask.add('', (context) => {
    // 判断是在浏览器访问还是iframe访问，走不同的入口
    const { appList, isAdmin, appId } = context;
    window.PROJECT_CONFIG.APP_ID = appId;
    appIdCache.setItem(context.appId);

    window.BKApp = new Vue({
        el: '#app',
        router: createRouter({ appList, isAdmin, appId }),
        store,
        i18n,
        render: h => h(EntryApp),
    });
});
entryTask.start();
