package com.yzjiang.common.plugin.mq.consumer.adapter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yzjiang.common.plugin.mq.consumer.bean.ConsumeBean;
import com.yzjiang.common.plugin.mq.serialization.ISerializer;
import com.yzjiang.common.plugin.mq.util.Commons;

public class KafkaConsumeAdapter implements ConsumeAdapter<Map<TopicPartition, OffsetAndMetadata>> {
	private static Logger log = LoggerFactory.getLogger(KafkaConsumeAdapter.class);
	private ISerializer<?> serializer;
	private KafkaConsumer<String, byte[]> consumer;
	private String topic;
	private boolean autoAck;
	
	public KafkaConsumeAdapter(Properties config, String topic, ISerializer<?> serializer, boolean autoAck) {
		Commons.checkArgument(config == null, "config不能为null");
		Commons.checkArgument(topic == null, "topic不能为null");
		Commons.checkArgument(serializer == null, "serializer不能为null");
		this.serializer = serializer;
		this.autoAck = autoAck;
		this.topic = topic;
		consumer = new KafkaConsumer<String, byte[]>(config);
		consumer.subscribe(Arrays.asList(topic), new ConsumerRebalanceListener() {
			public void onPartitionsRevoked(Collection<TopicPartition> collection) {
				log.info("onPartitionsRevoked");
			}

			public void onPartitionsAssigned(Collection<TopicPartition> collection) {
				log.info("onPartitionsAssigned");
			}
		});
	}
	
//	@Override
//	public <V> ConsumeBean<V, Map<TopicPartition, OffsetAndMetadata>> get(Long millis) throws InterruptedException {
//		List<ConsumeBean<V, Map<TopicPartition, OffsetAndMetadata>>> data = list(millis);
//		Commons.checkStatus(data.size() > 1, "确认pollSize是否等于1");
//		return data.size() > 0 ? data.get(0) : null;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> List<ConsumeBean<V, Map<TopicPartition, OffsetAndMetadata>>> poll(Long millis) throws InterruptedException {
		List<ConsumeBean<V, Map<TopicPartition, OffsetAndMetadata>>> data = new ArrayList<>();
		ConsumerRecords<String, byte[]> records = null;
		try {
			records = consumer.poll(Duration.ofMillis(millis));
		} catch (org.apache.kafka.common.errors.InterruptException e) {
			throw new InterruptedException(e.getMessage());
		}
		if(records == null || records.isEmpty()) {
			return data;
		}
		for (ConsumerRecord<String, byte[]> record : records) {
			Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
			offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));// 此处需加一，否则最后消费的数据会重复消费
			data.add(new ConsumeBean<V, Map<TopicPartition, OffsetAndMetadata>>((V) serializer.deserialize(record.value()), offsets));
		}
		return data;
	}

	@Override
	public void ack(Map<TopicPartition, OffsetAndMetadata> uniqueId) {
		if(autoAck) {
			return;
		}
		consumer.commitAsync(uniqueId, new OffsetCommitCallback() {
			@Override
			public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
				if(exception != null && offsets != null && offsets.size() > 0) {
					StringBuilder offsetInfo = new StringBuilder();
					for(Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
						offsetInfo.append("[").append(entry.getKey()).append("]").append(entry.getValue()).append(",");
					}
					offsetInfo.deleteCharAt(offsetInfo.length() - 1);
					log.warn("提交offset异常：" + offsetInfo, exception);
//				} else {// 测试使用
//					StringBuilder offsetInfo = new StringBuilder();
//					for(Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
//						offsetInfo.append("[").append(entry.getKey()).append("]").append(entry.getValue()).append(",");
//					}
//					offsetInfo.deleteCharAt(offsetInfo.length() - 1);
//					log.info("提交offset成功：" + offsetInfo);
				}
			}
		});
	}
	
	@Override
	public void close() {
		consumer.close(Duration.ofMillis(10000));
	}
	
	@Override
	public String toString() {
		return topic;
	}
}
