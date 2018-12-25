package com.yzjiang.common.plugin.mq.common;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import com.yzjiang.common.plugin.mq.util.Commons;
import com.yzjiang.common.plugin.mq.util.Value;

/**
 * @author Administrator
 */
public class RabbitmqService {
	
	/**
	 * 获取rabbitmq连接
	 * @param host
	 * @param port
	 * @param userName
	 * @param pass
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static Connection getConnection(String host, Integer port, String userName, String pass) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		if(port != null && port > 0) {
			factory.setPort(port);
		}
		if(pass != null && pass.length() > 0) {
			factory.setPassword(pass);
		}
		if(userName != null && userName.length() > 0) {
			factory.setUsername(userName);
		}
		factory.setAutomaticRecoveryEnabled(true);
//		factory.setChannelRpcTimeout(1000);// 此处的值，在RabbitmqConsumeAdapter中有用，经过测试发现该值用于首次建立连接的超时时间
		return factory.newConnection();
	}
	
	/**
	 * 获取消费者渠道实例
	 * @param connection
	 * @param prefetchCount
	 * @param exchange
	 * @param type
	 * @param exchangeDurable
	 * @param routingKey
	 * @param queueDurable
	 * @param queueExclusive
	 * @param queueAutoDelete
	 * @param queueArguments
	 * @return
	 * @throws IOException
	 */
	public static Channel getChannel(Connection connection, Integer prefetchCount, String exchange, BuiltinExchangeType type, boolean exchangeDurable, String routingKey, 
			Value<String> queueNameValue, boolean queueDurable, boolean queueExclusive, boolean queueAutoDelete, Map<String, Object> queueArguments) throws IOException {
		Commons.checkStatus(connection == null || !connection.isOpen(), "mq连接不可用");
		if(routingKey == null) {
			routingKey = "";
		}
		Channel channel = connection.createChannel();
		if(prefetchCount != null && prefetchCount > 0) {
			// 预取个数
			channel.basicQos(prefetchCount);
		}
		channel.exchangeDeclare(exchange, type, exchangeDurable);
		String queueName = queueNameValue.get();
		if(queueName == null) {
			queueName = channel.queueDeclare().getQueue();
			queueNameValue.set(queueName);
		} else {
			channel.queueDeclare(queueName, queueDurable, queueExclusive, queueAutoDelete, queueArguments);
		}
		channel.queueBind(queueName, exchange, routingKey);
		return channel;
	}
	
	/**
	 * 创建发布者渠道
	 * @param connection
	 * @param exchange
	 * @param type
	 * @param exchangeDurable
	 * @return
	 * @throws IOException
	 */
	public static Channel getChannel(Connection connection, String exchange, BuiltinExchangeType type, boolean exchangeDurable) throws IOException {
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchange, type, exchangeDurable);
		return channel;
	}
	
	/**
	 * 根据名称获取BuiltinExchangeType实例
	 * @param exchangeType
	 * @return
	 */
	public static BuiltinExchangeType getExchangeType(String exchangeType) {
		try {
			return BuiltinExchangeType.valueOf(exchangeType.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
