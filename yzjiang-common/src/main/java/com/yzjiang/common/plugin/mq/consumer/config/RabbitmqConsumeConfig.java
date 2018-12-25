package com.yzjiang.common.plugin.mq.consumer.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import com.yzjiang.common.plugin.mq.common.RabbitmqService;
import com.yzjiang.common.plugin.mq.consumer.adapter.ConsumeAdapter;
import com.yzjiang.common.plugin.mq.consumer.adapter.RabbitmqConsumeAdapter;
import com.yzjiang.common.plugin.mq.util.Commons;
import com.yzjiang.common.plugin.mq.util.Value;

public class RabbitmqConsumeConfig<V> extends ConsumeConfig<V, Long> {
//	private static final int MIN_TIMEOUT_MILLIS = 5000;// 最小等待返回数据的超时时间
	private String exchangeType = BuiltinExchangeType.FANOUT.name();// 交换机类型
	private boolean exchangeDurable = true;// 交换机是否持久化
	private String routingKey = "";
	private String queueName;// 队列名称
	private boolean queueDurable = true;// 队列是否持久化
	private boolean queueExclusive = false;
	private boolean queueAutoDelete = false;
	private Map<String, Object> queueArguments;
	
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
		this.routingKey = routingKey;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public boolean isQueueDurable() {
		return queueDurable;
	}

	public void setQueueDurable(boolean queueDurable) {
		this.queueDurable = queueDurable;
	}

	public boolean isQueueExclusive() {
		return queueExclusive;
	}

	public void setQueueExclusive(boolean queueExclusive) {
		this.queueExclusive = queueExclusive;
	}

	public boolean isQueueAutoDelete() {
		return queueAutoDelete;
	}

	public void setQueueAutoDelete(boolean queueAutoDelete) {
		this.queueAutoDelete = queueAutoDelete;
	}

	public Map<String, Object> getQueueArguments() {
		return queueArguments;
	}

	public void setQueueArguments(Map<String, Object> queueArguments) {
		this.queueArguments = queueArguments;
	}

	@Override
	public ConsumeAdapter<Long> getAdapter(int shardNum, int batchSize) {
		Commons.checkStatus(serverAddress == null || serverAddress.length() == 0, "MQ地址配置有误");
		Commons.checkStatus(topic == null || topic.length() == 0, "topic配置有误");
		String[] address = serverAddress.split(":");
		try {
			Connection connection = RabbitmqService.getConnection(address[0], address.length > 1 ? Integer.parseInt(address[1]) : null, userName, pass);
			String shardTopic = Commons.getShardTopic(topic, shardNum);
			Value<String> shardQueue = Value.of(Commons.getShardQueue(queueName, shardNum));
			Channel channel = RabbitmqService.getChannel(connection, prefetchCount, shardTopic, 
					RabbitmqService.getExchangeType(exchangeType), exchangeDurable, routingKey, 
					shardQueue, queueDurable, queueExclusive, queueAutoDelete, queueArguments);
			return new RabbitmqConsumeAdapter(channel, serializer, autoAck, shardQueue.get(), batchSize);
		} catch (NumberFormatException | IOException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}
	
}
