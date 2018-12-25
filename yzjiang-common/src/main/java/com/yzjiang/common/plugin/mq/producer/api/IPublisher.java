package com.yzjiang.common.plugin.mq.producer.api;

public interface IPublisher {
	
	/**
	 * 发布消息
	 * @param msg
	 */
	void publish(Object msg);
	
	/**
	 * 按照分片字段值计算后发布消息
	 * @param shardValue
	 * @param msg
	 */
	void publish(String shardValue, Object msg);
	
	void stop(long millis);
}
