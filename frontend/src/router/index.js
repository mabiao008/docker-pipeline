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

import Vue from 'vue';
import VueRouter from 'vue-router';
import _ from 'lodash';
import {
    leaveConfirm,
} from '@utils/assist';
import {
    routerCache,
} from '@utils/cache-helper';

import Entry from '@views/index';
import BusinessPermission from '@views/business-permission';
import NotFound from '@views/404';

import Home from '@views/home/routes';
import AccountManage from '@views/account-manage/routes';
import NotifyManage from '@views/notify-manage/routes';
import PublicScriptManage from '@views/public-script-manage/routes';
import Setting from '@views/setting/routes';
import WhiteIP from '@views/white-ip/routes';
import TaskManage from '@views/task-manage/routes';
import PlanManage from '@views/plan-manage/routes';
import FastExecution from '@views/fast-execution/routes';
import ScriptManage from '@views/script-manage/routes';
import ScriptTemplate from '@views/script-template/routes';
import CronJob from '@views/cron-job/routes';
import ExecutiveHistory from '@views/executive-history/routes';
import Dashboard from '@views/dashboard/routes';
import FileManage from '@views/file-manage/routes';
import TicketManage from '@views/ticket-manage/routes';
import ServiceState from '@views/service-state/routes';
import DetectRecords from '@views/detect-records/routes';
import DangerousRuleManage from '@views/dangerous-rule-manage/routes';
import Login from '@views/login/routes';

Vue.use(VueRouter);

let lastRouterHrefCache = '/';

const renderPageWithComponent = (route, component) => {
    if (route.component) {
    // eslint-disable-next-line no-param-reassign
        route.component = component;
    }
    if (route.children) {
        route.children.forEach((item) => {
            renderPageWithComponent(item);
        });
    }
};

export default ({ appList, isAdmin, appId }) => {
    // appid是否有效
    let isValidAppId = false;
    // appid是有有权限查看
    let hasAPPIdPermission = false;
    
    const appInfo = { hasPermission: true };
    // appId存在于业务列表中——有效的appid
    if (appInfo) {
        isValidAppId = true;
        // appId存在于业务列表中——有权限访问
        if (appInfo.hasPermission) {
            hasAPPIdPermission = true;
        }
    }

    // 生成路由配置
    const routes = [
        {
            path: '/',
            component: Entry,
            redirect: {
                name: 'home',
            },
            children: [
                Dashboard,
                ScriptTemplate,
            ],
        },
        {
            path: '/login',
            name: 'Login',
            component: Entry,
            redirect: {
                name: '/',
            },
            children: [
                Login,
            ],
        },
        {
            path: `/${appId}`,
            component: Entry,
            redirect: {
                name: 'home',
            },
            children: [
                AccountManage,
                NotifyManage,
                Home,
                TaskManage,
                PlanManage,
                FastExecution,
                ScriptManage,
                CronJob,
                ExecutiveHistory,
                FileManage,
                TicketManage,
                
            ],
        },
        {
            path: '/api_(execute|plan)/:id+',
            component: {
                render () {
                    return this._e(); // eslint-disable-line no-underscore-dangle
                },
            },
        },
        {
            path: '*',
            name: '404',
            component: NotFound,
        },
    ];

    if (!isValidAppId) {
        renderPageWithComponent(routes[1], NotFound);
    } else if (!hasAPPIdPermission) {
        renderPageWithComponent(routes[1], BusinessPermission);
    }

    // admin用户拥有系统设置功能
    if (isAdmin) {
        routes[0].children.push(PublicScriptManage);
        routes[0].children.push(WhiteIP);
        routes[0].children.push(Setting);
        routes[0].children.push(ServiceState);
        routes[0].children.push(DangerousRuleManage);
        routes[0].children.push(DetectRecords);
    }

    const router = new VueRouter({
        mode: 'history',
        routes,
        scrollBehavior () {
            return {
                x: 0, y: 0,
            };
        },
    });

    // 认证
    router.beforeEach((to, from, next) => {
        const token = localStorage.getItem('token');
        if (to.path !== '/login' && !token) {
            next({ path: '/login' });
        } else {
            next();
        }
    });

    const routerPush = router.push;
    const routerReplace = router.replace;

    // window.routerFlashBack === true 时查找路由缓存参数
    const routerFlaskBack = (params) => {
    /* eslint-disable no-param-reassign */
        params = _.cloneDeep(params);
        if (window.routerFlashBack) {
            // 路由回退
            const query = routerCache.getItem(params.name);
            if (query) {
                params.query = {
                    ...query,
                    ...params.query || {},
                };
            }
        } else {
            routerCache.clearItem(params.name);
        }
        lastRouterHrefCache = router.resolve(params).href;
        return params;
    };
    // 路由切换时
    // 检测页面数据的编辑状态——弹出确认框提示用户确认
    // 如果需要路由回溯（window.routerFlashBack === true）查找缓存是否有跳转目标的路由缓存数据
    router.push = (params, callback = () => {}) => {
    // 检测当前路由自定义离开确认交互
        let leaveConfirmHandler = leaveConfirm;
        const { currentRoute } = router;
        if (Object.prototype.hasOwnProperty.call(currentRoute, 'meta')
            && Object.prototype.hasOwnProperty.call(currentRoute.meta, 'leavaConfirm')
            && typeof currentRoute.meta.leavaConfirm === 'function') {
            leaveConfirmHandler = currentRoute.meta.leavaConfirm;
        }
        leaveConfirmHandler().then(() => {
            routerPush.call(router, routerFlaskBack(params));
            window.routerFlashBack = false;
        }, () => {
            callback();
        });
    };
    // 路由切换时
    // 检测页面数据的编辑状态——弹出确认框提示用户确认
    // 如果需要路由回溯（window.routerFlashBack === true）查找缓存是否有跳转目标的路由缓存数据
    router.replace = (params, callback = () => {}) => {
    // 检测当前路由自定义离开确认交互
        let leaveConfirmHandler = leaveConfirm;
        const { currentRoute } = router;
        if (Object.prototype.hasOwnProperty.call(currentRoute, 'meta')
            && Object.prototype.hasOwnProperty.call(currentRoute.meta, 'leavaConfirm')
            && typeof currentRoute.meta.leavaConfirm === 'function') {
            leaveConfirmHandler = currentRoute.meta.leavaConfirm;
        }
        leaveConfirmHandler().then(() => {
            routerReplace.call(router, routerFlaskBack(params));
            window.routerFlashBack = false;
        }, () => {
            callback();
        });
    };
    // 异步路由加载失败刷新页面
    router.onError((error) => {
        if (/Loading chunk (\d*) failed/.test(error.message)) {
            window.location.href = lastRouterHrefCache;
        }
    });
    window.$router = router;
    return router;
};
