package com.github.jadepeng.pipeline.config.dbmigrations;

import java.time.Instant;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.github.jadepeng.pipeline.config.Constants;
import com.github.jadepeng.pipeline.domain.Authority;
import com.github.jadepeng.pipeline.domain.Tag;
import com.github.jadepeng.pipeline.domain.User;
import com.github.jadepeng.pipeline.security.AuthoritiesConstants;

/**
 * Creates the initial database setup.
 */
@ChangeLog(order = "001")
public class InitialTag02 {

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

}
