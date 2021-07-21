package com.github.jadepeng.pipeline.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import com.github.jadepeng.pipeline.core.api.AgentApi;
import com.github.jadepeng.pipeline.core.bean.AgentInfo;
import com.github.jadepeng.pipeline.security.jwt.TokenProvider;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContext;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import feign.Feign;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import tech.jhipster.config.JHipsterProperties;

/**
 * 从EurekaServer 获取agent
 * @author jqpeng
 */
@Component
public class AgentDiscover {

    private static final String AGENT_NAME = "AGENT";
    private static final Map<String, AgentInfo> alivedAgents = new ConcurrentHashMap<String, AgentInfo>();
    private static String JWT_TOKEN;

    @Autowired
    public AgentDiscover(TokenProvider tokenProvider){
        JWT_TOKEN = tokenProvider.createFeignToken();
    }

    public Collection<AgentInfo> getAliveAgentInfo() {
        return alivedAgents.values();
    }

    // 每15s刷新下
    @Scheduled(fixedRate = 15000)
    public void discoverNginxNode() {
        List<String> nodes = getAliveAgents();

        for (String node : alivedAgents.keySet()) {
            if (!nodes.contains(node)) {
                alivedAgents.remove(node);
            }
        }

        for (String node : nodes) {
            try {
                AgentInfo info = this.getAgentApi(node).info();
                info.setAgentUrl(node);
                alivedAgents.put(node, info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public List<String> getAliveAgents() {
        List<String> instances = new ArrayList<>();
        List<Application> sortedApplications = getRegistry().getSortedApplications();
        Optional<Application> targetApp = sortedApplications.stream().filter(a->a.getName().equals(AGENT_NAME)).findFirst();
        if(targetApp.isPresent()){
            Application app = targetApp.get();
            for (InstanceInfo info : app.getInstances()) {
                instances.add(info.getHomePageUrl());
            }
        }
        return instances;
    }

    /**
     * build a agent Manager
     *
     * @param nodeUrl
     * @return
     */
    public AgentApi getAgentApi(String nodeUrl) {
        return Feign.builder()
                    .options(new Request.Options(1000, 3500))
                    .retryer(new Retryer.Default(5000, 5000, 3))
                    .requestInterceptor(new HeaderRequestInterceptor())
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .target(AgentApi.class, nodeUrl);
    }

    private PeerAwareInstanceRegistry getRegistry() {
        return getServerContext().getRegistry();
    }

    private EurekaServerContext getServerContext() {
        return EurekaServerContextHolder.getInstance().getServerContext();
    }

    public static class HeaderRequestInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
            template.header("Authorization",
                            "Bearer " + JWT_TOKEN);
        }
    }
}
