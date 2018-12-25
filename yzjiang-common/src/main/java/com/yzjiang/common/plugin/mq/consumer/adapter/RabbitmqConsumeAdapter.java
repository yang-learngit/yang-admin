package com.yzjiang.common.plugin.mq.consumer.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;

import com.yzjiang.common.plugin.mq.consumer.bean.ConsumeBean;
import com.yzjiang.common.plugin.mq.serialization.ISerializer;
import com.yzjiang.common.plugin.mq.util.Commons;

public class RabbitmqConsumeAdapter implements ConsumeAdapter<Long> {
//	private static Logger log = LoggerFactory.getLogger(RabbitmqConsumeAdapter.class);
//	private volatile boolean stop = false;
	private ISerializer<?> serializer;
	private boolean autoAck;
	private Channel channel;
	private String queueName;
	private int batchNum;
	
	public RabbitmqConsumeAdapter(Channel channel, ISerializer<?> serializer, boolean autoAck, String queueName, int batchNum) {
		Commons.checkArgument(channel == null, "channel不能为null");
		Commons.checkArgument(serializer == null, "serializer不能为null");
		this.serializer = serializer;
		this.autoAck = autoAck;
		this.channel = channel;
		this.queueName = queueName;
		this.batchNum = batchNum;
	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public <V> ConsumeBean<V, Long> get(Long millis) throws InterruptedException {
//		// 需设置Connection的channelRpcTimeout，经过测试发现不需要设置，如果设置会影响创建连接的超时时间
//		GetResponse response = null;
//		long startMillis = System.currentTimeMillis();
//		while(System.currentTimeMillis() - startMillis < millis && response == null) {
//			try {
//				response = channel.basicGet(queueName, autoAck);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
////			if(stop) {
////				throw new InterruptedException("停止消费");
////			}
//		}
//		if(response == null) {
//			return null;
//		}
////	    AMQP.BasicProperties props = response.getProps();
//	    long deliveryTag = response.getEnvelope().getDeliveryTag();
//	    return new ConsumeBean<V, Long>((V) serializer.deserialize(response.getBody()), deliveryTag);
//	}

	@Override
	public <V> List<ConsumeBean<V, Long>> poll(Long millis) throws InterruptedException {
		List<ConsumeBean<V, Long>> data = new ArrayList<>();
		long startMillis = System.currentTimeMillis();
		while(System.currentTimeMillis() - startMillis < millis && data.size() < batchNum) {
			ConsumeBean<V, Long> bean = get();
			if(bean != null) {
				data.add(bean);
			}
		}
		return data;
	}
	
	@SuppressWarnings("unchecked")
	private <V> ConsumeBean<V, Long> get() {
		GetResponse response = null;
		try {
			response = channel.basicGet(queueName, autoAck);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if(response == null) {
			return null;
		}
	    long deliveryTag = response.getEnvelope().getDeliveryTag();
	    return new ConsumeBean<V, Long>((V) serializer.deserialize(response.getBody()), deliveryTag);
	}

	@Override
	public void ack(Long uniqueId) {
		if(autoAck) {
			return;
		}
		try {
			channel.basicAck(uniqueId, false);// 确认收到消息
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void close() {
		try {
			if(channel.isOpen()) {
				channel.close();
			}
			Connection connection = channel.getConnection();
			if(connection.isOpen()) {
				connection.close(5000);
			}
		} catch (IOException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String toString() {
		return queueName;
	}
	
}
