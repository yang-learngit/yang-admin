package com.yzjiang.common.plugin.mq.consumer.config;

public enum DealType {
	ACK,// 删除消息，不再消费，目前支持的MQ包含：kafka、rabbitmq
//	REQUEUE,// 重新加入队列消费，目前支持的MQ包含：kafka、rabbitmq
	NOTHING;// 不做任何处理，下次连接Channel后，再次收到消息，目前支持的MQ包含：rabbitmq
	
	public static DealType get(String name) {
		try {
			return DealType.valueOf(name.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
