package com.yzjiang.common.plugin.mq.producer.config;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.yzjiang.common.plugin.mq.common.KafkaService;
import com.yzjiang.common.plugin.mq.producer.api.IPublisher;
import com.yzjiang.common.plugin.mq.producer.api.impl.KafkaPublisher;
import com.yzjiang.common.plugin.mq.util.Commons;

public class KafkaPublishConfig extends PublishConfig {
	private String acks = "1";// 0-写到网络层即返回；1-写到主节点即返回；-1/all-全部节点收到才返回
	private int retries = 3;// 异常重试次数
	private Integer batchSize;// 批量发送数据的大小，不配置则使用服务器默认值
	private long lingerMillis = 5L;// 同一个分区下的消息合并发送的最大延迟毫秒数
	private Long bufferMemory;// 不配置则使用服务器默认值
	private String compressionType = "none";// 压缩方式：none, gzip, snappy,lz4
	
	private int numPartitions = 10;// 主题包含几个分区，不可修改，应用写死在代码里
	private short replicationFactor = 1;// 主题包含几个备份节点，不可修改，应用写死在代码里

	public String getAcks() {
		return acks;
	}

	public void setAcks(String acks) {
		Commons.checkArgument(acks == null, "acks不能为null");
		this.acks = acks;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public long getLingerMillis() {
		return lingerMillis;
	}

	public void setLingerMillis(long lingerMillis) {
		this.lingerMillis = lingerMillis;
	}

	public Long getBufferMemory() {
		return bufferMemory;
	}

	public void setBufferMemory(Long bufferMemory) {
		this.bufferMemory = bufferMemory;
	}

	public String getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(String compressionType) {
		this.compressionType = compressionType;
	}
	
	public int getNumPartitions() {
		return numPartitions;
	}
	
	public void setNumPartitions(int numPartitions) {
		this.numPartitions = numPartitions;
	}
	
	public short getReplicationFactor() {
		return replicationFactor;
	}
	
	public void setReplicationFactor(short replicationFactor) {
		this.replicationFactor = replicationFactor;
	}

	@Override
	public IPublisher getPublisher(int shardNum) {
		Properties props = new Properties();
		props.put("bootstrap.servers", serverAddress);
		if (pass != null && pass.length() > 0) {
			props.put("ssl.key.password", pass);
		}
		props.put("acks", acks);
		props.put("retries", retries);
		if (batchSize != null) {
			props.put("batch.size", batchSize);
		}
		props.put("linger.ms", lingerMillis);
		if (bufferMemory != null) {
			props.put("buffer.memory", bufferMemory);
		}
		if (compressionType != null) {
			props.put("compression.type", compressionType);
		}
		props.put("key.serializer", StringSerializer.class.getName());// key序列化方式，此处固定使用该值，用户分片数据
		props.put("value.serializer", ByteArraySerializer.class.getName());// value序列化方式，此处固定使用该值，下层会做统一的反序列化
		
		String shardTopic = Commons.getShardTopic(topic, shardNum);
		try {
			if(!KafkaService.existsTopic(serverAddress, shardTopic)) {
				KafkaService.createTopicIgnoreExists(serverAddress, shardTopic, numPartitions, replicationFactor);
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		return new KafkaPublisher(props, shardTopic, serializer);
	}

}
