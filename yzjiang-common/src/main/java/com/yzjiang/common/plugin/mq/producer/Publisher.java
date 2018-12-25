package com.yzjiang.common.plugin.mq.producer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yzjiang.common.plugin.mq.producer.api.IPublisher;
import com.yzjiang.common.plugin.mq.producer.config.PublishConfig;
import com.yzjiang.common.plugin.mq.util.Commons;

public class Publisher implements IPublisher {
	private static Logger log = LoggerFactory.getLogger(Publisher.class);
	private int shardTopicNum = 1;
	private Map<Integer, IPublisher> shardPublish = new HashMap<>();// 不同topic使用不同的实例
	
	public Publisher(PublishConfig config) {
		Commons.checkArgument(config.getShardTopicNum() < 1, "shardTopicNum配置有误");
		shardTopicNum = config.getShardTopicNum();
		for(int i = 0; i < shardTopicNum; i++) {
			shardPublish.put(i, config.getPublisher(i));
		}
	}

	@Override
	public void publish(Object msg) {
		shardPublish.get(0).publish(msg);
	}

	@Override
	public void publish(String shardValue, Object msg) {
		int topicNo = 0;
		if(shardValue != null) {
			topicNo = shardValue.hashCode() % shardPublish.size();
		}
		shardPublish.get(topicNo).publish(shardValue, msg);
	}

	@Override
	public void stop(long millis) {
		for(IPublisher p : shardPublish.values()) {
			try {
				p.stop(millis);
			} catch (Exception e) {
				log.warn("停止消费异常：" + p, e);
			}
		}
	}
}
