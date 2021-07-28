package com.github.jadepeng.pipeline.core.bean;

/**
 * The Status enumeration.
 */
public enum Status {
    AVAIABLE, DISABLE, QUEUE, APPROVED, REJECTED, RUNNING, FINISHING, FINISHED,
    SUCCESS, FAIL;

    public boolean isRunning() {
        return this == Status.RUNNING;
    }
}
