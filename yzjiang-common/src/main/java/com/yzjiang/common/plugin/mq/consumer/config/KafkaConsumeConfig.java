package com.yzjiang.common.plugin.mq.consumer.config;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.yzjiang.common.plugin.mq.common.KafkaService;
import com.yzjiang.common.plugin.mq.consumer.adapter.ConsumeAdapter;
import com.yzjiang.common.plugin.mq.consumer.adapter.KafkaConsumeAdapter;
import com.yzjiang.common.plugin.mq.util.Commons;

public class KafkaConsumeConfig<V> extends ConsumeConfig<V, Map<TopicPartition, OffsetAndMetadata>> {
	private String consumeGroup = "DEFAULT_GROUP";// 消费组，一个组内的消费者对消息互斥消费
	private int autoAckInterval = 1000;// 自动提交间隔毫秒数，如果自动提交消息消费位置，则该值设置提交间隔
	
	private int numPartitions = 10;// 主题包含几个分区，不可修改，应用写死在代码里
	private short replicationFactor = 1;// 主题包含几个备份节点，不可修改，应用写死在代码里
	
	public String getConsumeGroup() {
		return consumeGroup;
	}

	public void setConsumeGroup(String consumeGroup) {
		Commons.checkArgument(consumeGroup == null, "consumeGroup不能为null");
		this.consumeGroup = consumeGroup;
	}

	public int getAutoAckInterval() {
		return autoAckInterval;
	}
	
	public void setAutoAckInterval(int autoAckInterval) {
		this.autoAckInterval = autoAckInterval;
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
	public ConsumeAdapter<Map<TopicPartition, OffsetAndMetadata>> getAdapter(int shardNum, int batchSize) {
		String shardTopic = Commons.getShardTopic(topic, shardNum);
		try {
			if(!KafkaService.existsTopic(serverAddress, shardTopic)) {
				KafkaService.createTopicIgnoreExists(serverAddress, shardTopic, numPartitions, replicationFactor);
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		return new KafkaConsumeAdapter(getConfig(), shardTopic, serializer, autoAck);
	}
	
	private Properties getConfig() {
		Properties props = new Properties();
		props.put("bootstrap.servers", serverAddress);
		if(pass != null && pass.length() > 0) {
			props.put("ssl.key.password", pass);
		}
		props.put("group.id", consumeGroup);// 设置消费组，一个消费组内的消费者消费消息是互斥的
		props.put("enable.auto.commit", autoAck);// 是否启动自动提交offset（ack）
		props.put("auto.commit.interval.ms", autoAckInterval);// 如果启用自动提交offset，则此处为提交间隔
		if(prefetchCount != null && prefetchCount > 0) {
			props.put("max.poll.records", prefetchCount);// 一次最大拉取的数量，可用于实现单条数据拉取，批量数据拉取
		}
		props.put("max.poll.interval.ms", maxWaitMillis);// 一次拉取数据最大等待时长
		props.put("key.deserializer", StringDeserializer.class.getName());// key序列化方式，此处固定使用该值，用户分片数据
		props.put("value.deserializer", ByteArrayDeserializer.class.getName());// value序列化方式，此处固定使用该值，下层会做统一的反序列化
		return props;
	}
}
