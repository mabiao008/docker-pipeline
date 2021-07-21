package com.github.jadepeng.pipeline.agent.core;

import com.github.jadepeng.pipeline.core.bean.OperationalSystemInfo;
import com.github.jadepeng.pipeline.core.bean.OperationalSystemType;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.regex.Matcher;

/**
 * 操作系统
 */
public class OperationalSystem {

    private static Logger logger = LoggerFactory.getLogger(OperationalSystem.class);

    private OperationalSystem() {

    }

    public static OperationalSystemInfo info() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        OperationalSystemInfo operationalSystemInfo = new OperationalSystemInfo(operatingSystemMXBean.getName(),
            operatingSystemMXBean.getArch(), operatingSystemMXBean.getVersion());
        String operationalSystem = operationalSystemInfo.getName().toLowerCase();
        if (operationalSystem.contains("windows")) {
            operationalSystemInfo.setOperationalSystemType(
                    OperationalSystemType.WINDOWS);
        } else if (operationalSystem.contains("linux")) {
            operationalSystemInfo.setOperationalSystemType(OperationalSystemType.LINUX);
            operationalSystemInfo.setDistribution(distribution());
        } else if (operationalSystem.contains("mac") || operationalSystem.contains("darwin")) {
            operationalSystemInfo.setOperationalSystemType(OperationalSystemType.MAC);
        } else {
            operationalSystemInfo.setOperationalSystemType(OperationalSystemType.UNKNOW);
        }
        return operationalSystemInfo;
    }

    private static String distribution() {
        try {

            if (new File("/etc/os-release").exists()) {
                String content = FileUtils.readFileToString(new File("/etc/os-release"), "UTF-8");
                Matcher matcher = PatternConsts.PRETTY_NAME_PATTERN.matcher(content);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            } else if (new File("/etc/issue").exists()) {
                FileUtils.readFileToString(new File("/etc/issue"), "UTF-8").trim();
            }
        } catch (IOException e) {
            logger.error("Could not find /etc/os-release and /etc/issue");
        }
        return "unknow";
    }
}
