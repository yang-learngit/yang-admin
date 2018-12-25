package com.yzjiang.common.plugin.mq.producer.api.impl;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.yzjiang.common.plugin.mq.producer.api.IPublisher;
import com.yzjiang.common.plugin.mq.serialization.ISerializer;
import com.yzjiang.common.plugin.mq.util.Commons;

public class KafkaPublisher implements IPublisher {
	private Producer<String, byte[]> producer;
	private ISerializer<Object> serializer;
	private String topic;
	
	@SuppressWarnings("unchecked")
	public KafkaPublisher(Properties config, String topic, ISerializer<?> serializer) {
		Commons.checkArgument(config == null, "config不能为null");
		Commons.checkArgument(topic == null, "topic不能为null");
		Commons.checkArgument(serializer == null, "serializer不能为null");
		this.producer = new KafkaProducer<>(config);
		this.topic = topic;
		this.serializer = (ISerializer<Object>) serializer;
	}
	
	@Override
	public void publish(Object msg) {
		publish(null, msg);
	}

	@Override
	public void publish(String shardValue, Object msg) {
//		String shardTopic = Commons.getShardTopic(shardValue, topic, shardNum);
		// 如果kafka的分片数量与shardNum有倍数关系，则数据在kafka一个主题中不会均匀的分布到分区中，是否需要重写hash，通过查看源码，发现kafka使用org.apache.kafka.common.utils.Utils.murmur2计算hash值，与本身hash是有差异的
		producer.send(new ProducerRecord<String, byte[]>(topic, shardValue, serializer.serialize(msg)));
	}
	
	@Override
	public void stop(long millis) {
		producer.close(millis, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public String toString() {
		return topic;
	}
}
