package com.yzjiang.common.plugin.mq.consumer.config;

import com.yzjiang.common.plugin.mq.common.BaseConfig;
import com.yzjiang.common.plugin.mq.consumer.adapter.ConsumeAdapter;
import com.yzjiang.common.plugin.mq.consumer.api.IBatchConsume;
import com.yzjiang.common.plugin.mq.consumer.api.IConsume;
import com.yzjiang.common.plugin.mq.consumer.bind.BatchConsumeBind;
import com.yzjiang.common.plugin.mq.consumer.bind.ConsumeBind;
import com.yzjiang.common.plugin.mq.consumer.bind.ConsumeBindAbs;
import com.yzjiang.common.plugin.mq.util.Commons;

/**
 * 通用配置
 * @Description 
 * 
 * @author liuzhao
 * @date 2018年10月7日 上午9:56:31
 */
public abstract class ConsumeConfig<V, T> extends BaseConfig {
	protected Integer prefetchCount;// 客户端预取数量
	protected boolean autoAck = false;// 是否自动提交消息消费位置
	protected boolean skipNull = true;// 如果在规定条件内未获取到数据时，是否回调
//	protected int pollSize = 1;// 一次拉取的最大数量，等于1为单条消费，大于1为批量消费
	protected int maxWaitMillis = 500;// 一次拉取数据最大等待时间
	protected int retryTimes = 3;// 消费异常时重试次数，不包含首次请求
	protected String dealType = DealType.ACK.name();// 达到最大重试次数依然消费失败时的处理方式
	protected byte consumerNum = 1;// 开启的消费者数量，每个消费者独占一个线程，该配置是针对一个topic的，如果是rabbitmq分片，则每个topic都有这么多个线程消费
	
	public Integer getPrefetchCount() {
		return prefetchCount;
	}
	
	public void setPrefetchCount(Integer prefetchCount) {
		Commons.checkArgument(prefetchCount != null && prefetchCount <= 0, "prefetchCount必须大于0");
		this.prefetchCount = prefetchCount;
	}
	
	public boolean isAutoAck() {
		return autoAck;
	}
	
	public void setAutoAck(boolean autoAck) {
		this.autoAck = autoAck;
	}
	
	public boolean isSkipNull() {
		return skipNull;
	}
	
	public void setSkipNull(boolean skipNull) {
		this.skipNull = skipNull;
	}
	
//	public int getPollSize() {
//		return pollSize;
//	}
	
//	public void setPollSize(int pollSize) {
//		Commons.checkArgument(pollSize < 1, "pollSize必须大于0");
//		this.pollSize = pollSize;
//	}
	
	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}
	
	public void setMaxWaitMillis(int maxWaitMillis) {
		Commons.checkArgument(maxWaitMillis < 1, "maxWaitMillis必须大于0");
		this.maxWaitMillis = maxWaitMillis;
	}
	
	public int getRetryTimes() {
		return retryTimes;
	}
	
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
	
	public String getDealType() {
		return dealType;
	}
	
	public void setDealType(String dealType) {
		Commons.checkArgument(dealType == null || DealType.get(dealType) == null, "dealType配置有误，参考类DealType");
		this.dealType = dealType;
	}
	
	public byte getConsumerNum() {
		return consumerNum;
	}
	
	public void setConsumerNum(byte consumerNum) {
		Commons.checkArgument(consumerNum < 0, "consumerNum必须大于0");
		this.consumerNum = consumerNum;
	}
	
	public ConsumeBindAbs<V, T> getBind(IConsume<V> consume, int shardNum) {
		return setCommonConfig(new ConsumeBind<>(getAdapter(shardNum, 1), consume));
	}

	public ConsumeBindAbs<V, T> getBatchBind(IBatchConsume<V> consume, int shardNum, int batchSize) {
		return setCommonConfig(new BatchConsumeBind<>(getAdapter(shardNum, batchSize), consume, batchSize));
	}
	
	public abstract ConsumeAdapter<T> getAdapter(int shardNum, int batchSize);
	
	private ConsumeBindAbs<V, T> setCommonConfig(ConsumeBindAbs<V, T> bind) {
		bind.setDealType(DealType.get(dealType));
		bind.setReadTimeoutMillis(maxWaitMillis);
		bind.setRetryTimes(retryTimes);
		bind.setSkipNull(skipNull);
		return (ConsumeBindAbs<V, T>) bind;
	}
}
