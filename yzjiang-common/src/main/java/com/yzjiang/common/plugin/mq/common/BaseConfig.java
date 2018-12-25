package com.yzjiang.common.plugin.mq.common;

import com.yzjiang.common.plugin.mq.serialization.ISerializer;
import com.yzjiang.common.plugin.mq.serialization.StringSerializer;
import com.yzjiang.common.plugin.mq.util.Commons;

/**
 * @author Administrator
 */
public class BaseConfig {
	/**
	 * MQ服务地址，多个地址使用英文逗号分隔，如：192.168.1.1:9092,192.168.1.1:9093
	 */
	protected String serverAddress;
	/**
	 * 认证用户
	 */
	protected String userName;
	/**
	 * MQ服务连接密码
	 */
	protected String pass;
	/**
	 * 订阅的主题名称，如果为kafka，则不允许为匹配模式
	 */
	protected String topic;
	/**
	 * topic分片数量，应用层分片，分片后会有多个主题（交换机）及其对应的队列
	 */
	protected int shardTopicNum = 1;
	/**
	 * 消息序列化方式
	 */
	protected ISerializer<?> serializer = new StringSerializer();
	
	public String getServerAddress() {
		return serverAddress;
	}
	
	public void setServerAddress(String serverAddress) {
		Commons.checkArgument(serverAddress == null, "serverAddress不能为null");
		this.serverAddress = serverAddress;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		Commons.checkArgument(topic == null, "topic不能为null");
		this.topic = topic;
	}

	public int getShardTopicNum() {
		return shardTopicNum;
	}
	
	public void setShardTopicNum(int shardTopicNum) {
		Commons.checkArgument(shardTopicNum < 1, "shardTopicNum必须大于0");
		this.shardTopicNum = shardTopicNum;
	}
	
	public ISerializer<?> getSerializer() {
		return serializer;
	}

	public void setSerializer(ISerializer<?> serializer) {
		Commons.checkArgument(serializer == null, "serializer不能为null");
		this.serializer = serializer;
	}
	
}
