package com.yzjiang.common.plugin.mq.producer.config;

import com.yzjiang.common.plugin.mq.common.BaseConfig;
import com.yzjiang.common.plugin.mq.producer.api.IPublisher;

public abstract class PublishConfig extends BaseConfig {
	
	/**
	 * 获取发布实例
	 */
	public abstract IPublisher getPublisher(int shardNum);
}
