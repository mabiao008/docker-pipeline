package com.github.jadepeng.pipeline.utils;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

/**
 * @author jqpeng (jqpeng@iflytek.com)
 * @Description:
 * @date 2018/9/12 11:33
 */
public class RuntimeBuilder {
    private String command;
    private String directory;
    private Map<String, String> enviroment;
    private Integer timeout;

    private RuntimeBuilder() {
    }

    /**
     *  创建RuntimeBuilder
     * @return RuntimeBuilder
     */
    public static RuntimeBuilder newBuilder() {
        return new RuntimeBuilder();
    }

    /**
     *  设置命令
     * @param command  命令
     * @return RuntimeBuilder
     */
    public RuntimeBuilder withCommand(String command) {
        this.command = command;
        return this;
    }

    /**
     *  设置工作目录
     * @param directory 工作目录
     * @return RuntimeBuilder
     */
    public RuntimeBuilder withDirectory(String directory) {
        this.directory = directory;
        return this;
    }

    /**
     *  设置环境变量
     * @param enviroment 环境变量
     * @return RuntimeBuilder
     */
    public RuntimeBuilder withEnviroment(Map<String, String> enviroment) {
        this.enviroment = enviroment;
        return this;
    }

    /**
     *  设置超时时间
     * @param timeout 超时时间
     * @return RuntimeBuilder
     */
    public RuntimeBuilder withTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     *  执行命令
     * @return RuntimeResult
     */
    public RuntimeResult execute() {
        try {
            ProcessExecutor processExecutor = new ProcessExecutor();
            processExecutor.commandSplit(this.command);
            processExecutor.readOutput(true);
            if (!StringUtils.isEmpty(this.directory)) {
                processExecutor.directory(new File(this.directory));
            }

            if (this.enviroment != null) {
                processExecutor.environment(this.enviroment);
            }

            if (this.timeout != null) {
                processExecutor.timeout((long)this.timeout.intValue(), TimeUnit.SECONDS);
            }

            ProcessResult processResult = processExecutor.execute();
            String output = processResult.outputUTF8();
            return processResult.getExitValue() == 0 ? new RuntimeResult(RuntimeResultType.SUCCESS, output) : new RuntimeResult(RuntimeResultType.ERROR, output);
        } catch (Exception var4) {
            return new RuntimeResult(RuntimeResultType.ERROR, ExceptionUtils.getStackTrace(var4));
        }
    }

    /**
     *  执行结果
     */
    public static class RuntimeResult implements Serializable {
        private RuntimeResultType runtimeResultType;
        private String output;

        public  static RuntimeResult OK(){
            return  new RuntimeResult(RuntimeResultType.SUCCESS,"success");
        }

        public  static RuntimeResult Error( String output){
            return  new RuntimeResult(RuntimeResultType.ERROR,output);
        }

        public RuntimeResult(RuntimeResultType runtimeResultType, String output) {
            this.runtimeResultType = runtimeResultType;
            this.output = output;
        }

        public String getOutput() {
            return this.output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        public RuntimeResultType getRuntimeResultType() {
            return this.runtimeResultType;
        }

        public void setRuntimeResultType(RuntimeResultType runtimeResultType) {
            this.runtimeResultType = runtimeResultType;
        }

        public Boolean is(RuntimeResultType runtimeResultType) {
            return this.runtimeResultType.equals(runtimeResultType);
        }

        public Boolean isSuccess() {
            return this.is(RuntimeResultType.SUCCESS);
        }

        public Boolean isError() {
            return this.is(RuntimeResultType.ERROR);
        }
    }

    /**
     *  执行结果类型
     */
    public enum RuntimeResultType {
        ERROR,
        SUCCESS;

        private RuntimeResultType() {
        }
    }



}
