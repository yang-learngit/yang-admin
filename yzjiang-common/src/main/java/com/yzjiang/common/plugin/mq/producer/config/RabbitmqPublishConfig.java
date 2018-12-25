package com.yzjiang.common.plugin.mq.producer.config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import com.yzjiang.common.plugin.mq.common.RabbitmqService;
import com.yzjiang.common.plugin.mq.producer.api.IPublisher;
import com.yzjiang.common.plugin.mq.producer.api.impl.RabbitmqPublisher;
import com.yzjiang.common.plugin.mq.util.Commons;

public class RabbitmqPublishConfig extends PublishConfig {
	private String exchangeType = BuiltinExchangeType.FANOUT.name();// 交换机类型
	private boolean exchangeDurable = true;// 交换机是否持久化
	private String routingKey = "";
	private boolean msgPersistent = true;// 消息是否持久化
	
//	private boolean useNio;// TODO 官方文档说明默认使用BIO，考虑使用NIO调优参数
	
	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		Commons.checkArgument(exchangeType == null || RabbitmqService.getExchangeType(exchangeType) == null, "exchangeType配置有误，参考类BuiltinExchangeType");
		this.exchangeType = exchangeType;
	}

	public boolean isExchangeDurable() {
		return exchangeDurable;
	}

	public void setExchangeDurable(boolean exchangeDurable) {
		this.exchangeDurable = exchangeDurable;
	}
	
	public String getRoutingKey() {
		return routingKey;
	}
	
	public void setRoutingKey(String routingKey) {
		Commons.checkArgument(routingKey == null, "routingKey不能为null");
		this.routingKey = routingKey;
	}

	@Override
	public IPublisher getPublisher(int shardNum) {
		Commons.checkStatus(serverAddress == null || serverAddress.length() == 0, "MQ地址配置有误");
		Commons.checkStatus(topic == null || topic.length() == 0, "topic配置有误");
		String[] address = serverAddress.split(":");
		try {
			Connection connection = RabbitmqService.getConnection(address[0], address.length > 1 ? Integer.parseInt(address[1]) : null, userName, pass);
			String shardTopic = Commons.getShardTopic(topic, shardNum);
			Channel channel = RabbitmqService.getChannel(connection, shardTopic, RabbitmqService.getExchangeType(exchangeType), exchangeDurable);
			return new RabbitmqPublisher(channel, shardTopic, msgPersistent, serializer, routingKey);
		} catch (NumberFormatException | IOException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}

}
