package com.yzjiang.common.plugin.mq.producer.api.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import com.yzjiang.common.plugin.mq.producer.api.IPublisher;
import com.yzjiang.common.plugin.mq.serialization.ISerializer;
import com.yzjiang.common.plugin.mq.util.Commons;

public class RabbitmqPublisher implements IPublisher {
	private Channel channel;
	private ISerializer<Object> serializer;
	private boolean persistent;
	private String exchange;
	private String routingKey = "";
	
	@SuppressWarnings("unchecked")
	public RabbitmqPublisher(Channel channel, String exchange, boolean persistent, ISerializer<?> serializer, String routingKey) {
		Commons.checkArgument(channel == null, "channel不能为null");
		Commons.checkArgument(exchange == null, "exchange不能为null");
		Commons.checkArgument(serializer == null, "serializer不能为null");
		this.channel = channel;
		this.exchange = exchange;
		this.persistent = persistent;
		this.serializer = (ISerializer<Object>) serializer;
		this.routingKey = routingKey == null ? "" : routingKey;
	}
	
	@Override
	public void publish(Object msg) {
		publish(null, msg);
	}

	@Override
	public void publish(String shardValue, Object msg) {
//		String shardExchange = Commons.getShardTopic(shardValue, exchange, shardNum);
		BasicProperties properties = null;// 该配置是否可共享？，看源码好像不可以，包含了不可共享的变量
		if(persistent) {
			AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
			builder.deliveryMode(2);
			properties = builder.build();
//			properties = MessageProperties.PERSISTENT_TEXT_PLAIN
		}
		try {
			channel.basicPublish(exchange, routingKey, properties, serializer.serialize(msg));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void stop(long millis) {
		try {
			if(channel.isOpen()) {
				channel.close();
			}
			Connection connection = channel.getConnection();
			if(connection.isOpen()) {
				connection.close((int) millis);
			}
		} catch (IOException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String toString() {
		return exchange;
	}
}
