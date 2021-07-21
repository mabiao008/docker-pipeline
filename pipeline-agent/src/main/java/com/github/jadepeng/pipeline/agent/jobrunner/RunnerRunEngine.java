package com.github.jadepeng.pipeline.agent.jobrunner;

import com.github.jadepeng.pipeline.core.bean.Pipeline;

public interface RunnerRunEngine {

    void setUp();

    void exec();

    void kill();
}


