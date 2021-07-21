package com.github.jadepeng.pipeline.agent.jobrunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.github.jadepeng.pipeline.core.PipelineLogger;

import lombok.SneakyThrows;

public class FilePipelingLogger implements PipelineLogger {

    private FileOutputStream outLogger;

    private PipelineLogger wrapLogger;

    public FilePipelingLogger(File logFile) throws FileNotFoundException {
        outLogger = new FileOutputStream(logFile);
    }

    public void setWriterLogger(PipelineLogger logger) {
        this.wrapLogger = logger;
    }

    @SneakyThrows
    @Override
    public void appendLog(String log) {
        if (this.wrapLogger != null) {
            this.wrapLogger.appendLog(log );
        }
        this.outLogger.write((log + "\n").getBytes("utf-8"));
        this.outLogger.flush();
    }

    public void close() {
        try {
            this.outLogger.flush();
            this.outLogger.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
