package com.github.jadepeng.pipeline.config.dbmigrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.github.jadepeng.pipeline.domain.*;

/**
 * Creates the initial database setup.
 */
@ChangeLog(order = "001")
public class InitialCoreData {

    @ChangeSet(order = "01", author = "initiator", id = "01-addTags")
    public void addTags(MongockTemplate mongoTemplate) {
        Tag tag = new Tag();
        tag.setName("脚本任务");
        tag.setId("script");
        mongoTemplate.save(tag);

        tag.setName("jar程序");
        tag.setId("jar");
        mongoTemplate.save(tag);

        tag.setName("机器学习");
        tag.setId("ml");
        mongoTemplate.save(tag);
    }

    @ChangeSet(order = "01", author = "initiator", id = "01-addApp")
    public void addApp(MongockTemplate mongoTemplate) {
        App tag = new App();
        tag.setName("aimind");
        tag.setId("aimind");
        mongoTemplate.save(tag);

        tag.setName("ainote");
        tag.setId("ainote");
        mongoTemplate.save(tag);
    }

    @ChangeSet(order = "01", author = "initiator", id = "01-addDockerImage")
    public void addDockerImage(MongockTemplate mongoTemplate) {
        DockerImage dockerImage = new DockerImage();
        dockerImage.setName("python");
        dockerImage.setUrl("python:1.0");
        dockerImage.setId("python");
        mongoTemplate.save(dockerImage);

        dockerImage.setName("java 1.8");
        dockerImage.setUrl("java:1.8");
        dockerImage.setId("java8");
        mongoTemplate.save(dockerImage);
    }

}
