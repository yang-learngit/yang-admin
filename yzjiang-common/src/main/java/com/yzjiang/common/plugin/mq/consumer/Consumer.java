package com.yzjiang.common.plugin.mq.consumer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yzjiang.common.plugin.mq.consumer.api.IBatchConsume;
import com.yzjiang.common.plugin.mq.consumer.api.IConsume;
import com.yzjiang.common.plugin.mq.consumer.bind.ConsumeBindAbs;
import com.yzjiang.common.plugin.mq.consumer.config.ConsumeConfig;
import com.yzjiang.common.plugin.mq.util.Commons;

/**
 * @author Administrator
 */
public class Consumer<V> {
	private static Logger log = LoggerFactory.getLogger(Consumer.class);
	private List<ConsumeBindAbs<V, ?>> consumers = new ArrayList<>();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Consumer(ConsumeConfig config, IConsume<V> consume) {
		Commons.checkArgument(consume == null, "consume不能为null");
//		Commons.checkArgument(config.getPollSize() != 1, "该构造方法参数中的pollSize必须等于1");
//		config.setPollSize(1);
		int shardTopicNum = config.getShardTopicNum();
		for(int k = 0; k < shardTopicNum; k++) {
			for(int i = 0; i < config.getConsumerNum(); i++) {// 此处考虑kafka在增加消费者时会平衡消费者消费的分区，所以先创建后启动
				consumers.add(config.getBind(consume, k));
			}
		}
		startConsumer();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Consumer(ConsumeConfig config, int batchSize, IBatchConsume<V> consume) {
		Commons.checkArgument(consume == null, "consume不能为null");
		Commons.checkArgument(batchSize < 1, "batchSize必须大于1");
//		Commons.checkArgument(config.getPollSize() <= 1, "该构造方法参数中的pollSize必须大于1");
		int shardTopicNum = config.getShardTopicNum();
		for(int k = 0; k < shardTopicNum; k++) {
			for(int i = 0; i < config.getConsumerNum(); i++) {// 此处考虑kafka在增加消费者时会平衡消费者消费的分区，所以先创建后启动
				consumers.add(config.getBatchBind(consume, k, batchSize));
			}
		}
		startConsumer();
	}
	
	private void startConsumer() {
		int i = 1;
		for(Runnable r : consumers) {
			Thread t = new Thread(r);
			t.setName("CONSUMER-" + r + "-Thread-" + i++);
			t.start();
		}
	}
	
	public void stop(long millis) {
		for(ConsumeBindAbs<V, ?> r : consumers) {
			try {
				r.stop(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				log.warn("停止消费异常：" + r, e);
			}
		}
	}
}
