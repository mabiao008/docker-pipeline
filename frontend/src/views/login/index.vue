<!--
 * Tencent is pleased to support the open source community by making BK-JOB蓝鲸智云作业平台 available.
 *
 * Copyright (C) 2021 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-JOB蓝鲸智云作业平台 is licensed under the MIT License.
 *
 * License for BK-JOB蓝鲸智云作业平台:
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
-->

<template>
    <smart-action class="create-script-page" offset-target="bk-form-content">
        <jb-form :model="formData" :rules="rules" ref="form">
            <jb-form-item :label="$t('用户名')" required property="name">
                <div class="script-name input">
                    <jb-input
                        v-model="formData.name"
                        :placeholder="$t('请输入用户名')"
                        :maxlength="60" />
                </div>
            </jb-form-item>
            <jb-form-item :label="$t('密码')" property="password">
                <div class="script-name input">
                    <bk-input
                        v-model="formData.password"
                        :placeholder="$t('请输入用户名')"
                        type="password" />
                </div>
            </jb-form-item>
        </jb-form>
        <template #action>
            <bk-button class="w120 mr10" :loading="isSbumiting" theme="primary" @click="handleSubmit">{{ $t('登录') }}</bk-button>
            <bk-button theme="default" @click="handleCancel">{{ $t('取消') }}</bk-button>
        </template>
    </smart-action>
</template>
<script>
    import I18n from '@/i18n';
    import AccountManageService from '@service/account-manage';
    import {
        getOffset,
    } from '@utils/assist';
    import {
        scriptNameRule,
        scriptVersionRule,
    } from '@utils/validator';
    import JbInput from '@components/jb-input';

    export default {
        name: '',
        components: {
            JbInput,
        },
        data () {
            return {
                isContentLoading: false,
                isSbumiting: false,
                contentHeight: 480,
                formData: {
                    name: '',
                    password: '',
                },
            };
        },
        created () {
            this.rules = {
                name: [
                    {
                        required: true,
                        message: I18n.t('用户名必填'),
                        trigger: 'blur',
                    },
                    {
                        validator: scriptNameRule.validator,
                        message: scriptNameRule.message,
                        trigger: 'blur',
                    },
                ],
                password: [
                    {
                        required: true,
                        message: I18n.t('密码必填'),
                        trigger: 'blur',
                    },
                    {
                        validator: scriptVersionRule.validator,
                        message: scriptVersionRule.message,
                        trigger: 'blur',
                    },
                ],
            };
        },
        mounted () {
            this.init();
        },
        methods: {
            /**
             * @desc 计算内容区的高度
             */
            init () {
                const contentOffsetTop = getOffset(this.$refs.content).top;
                const contentHeight = window.innerHeight - contentOffsetTop + 20;
                this.contentHeight = contentHeight > 480 ? contentHeight : 480;
            },

            /**
             * @desc 保存脚本
             */
            handleSubmit () {
                this.isSbumiting = true;
                this.$refs.form.validate()
                    .then(() => {
                        AccountManageService.authenticate({
                            username: this.formData.name,
                            rememberMe: true,
                            password: this.formData.password,
                        }).then((data) => {
                            if (data.id_token) {
                                localStorage.setItem('token', data.id_token);
                                window.changeAlert = false;
                                this.$router.push({
                                    path: '/',
                                });
                            } else {
                                alert('login error');
                            }
                        });
                    })
                    .finally(() => {
                        this.isSbumiting = false;
                    });
            },
            /**
             * @desc 取消新建
             */
            handleCancel () {
                this.formData.name = '';
                this.formData.password = '';
            },
        },
    };
</script>
<style lang='postcss' scoped>
    .create-script-page {
        .input {
            width: 510px;
            background: #fff;
        }
    }

    .script-name-tips {
        padding-right: 12px;
        font-size: 12px;
        line-height: 16px;
        color: #63656e;

        .row {
            position: relative;
            padding-right: 12px;
            padding-left: 12px;

            &::before {
                position: absolute;
                top: 6px;
                left: 0;
                width: 4px;
                height: 4px;
                background: currentColor;
                border-radius: 50%;
                content: '';
            }
        }
    }
</style>
