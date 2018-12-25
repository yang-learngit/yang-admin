package com.yzjiang.common.plugin.mq.consumer.bind;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import com.yzjiang.common.plugin.mq.consumer.adapter.ConsumeAdapter;
import com.yzjiang.common.plugin.mq.consumer.bean.ConsumeBean;
import com.yzjiang.common.plugin.mq.consumer.config.DealType;

public abstract class ConsumeBindAbs<V, T> implements Runnable {
	private static Logger log = LoggerFactory.getLogger(ConsumeBindAbs.class);
	
	protected static final long MIN_POLL_MILLIS = 100;// 停止应用时最大延迟时间
	protected boolean stop = false;// 此处不使用volatile会不会提高性能
//	protected Thread runThread;
	protected CountDownLatch countDown = new CountDownLatch(1);
	
	protected ConsumeAdapter<T> adapter;
	protected Integer retryTimes = 0;
	protected long readTimeoutMillis = 5000;
	protected DealType dealType = DealType.ACK;
	protected boolean skipNull = false;
	protected List<ConsumeBean<V, T>> cacheBeans = new ArrayList<>();// 用于批量拉取数据时条数超过配置时缓存数据，长度不会超过配置长度的两倍，做这个处理主要是kafka停止消费时不能被打断、不能在不同的线程关闭消费者，具体参考ConsumeBindAbs的stop注释，如果等待ConsumeAdapter停止，可能最大需要等待readTimeoutMillis，如果使用get一次拿一条可能会影响kafka吞吐量，总和考虑使用这个方法
	private int maxPoll;// 当cacheBeans中的数据条数小于该值时填充数据
	
	ConsumeBindAbs(ConsumeAdapter<T> adapter, int maxPoll) {
		this.adapter = adapter;
		this.maxPoll = maxPoll;
	}
	
	public Integer getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}

	public long getReadTimeoutMillis() {
		return readTimeoutMillis;
	}

	public void setReadTimeoutMillis(long readTimeoutMillis) {
		this.readTimeoutMillis = readTimeoutMillis;
	}

	public DealType getDealType() {
		return dealType;
	}

	public void setDealType(DealType dealType) {
		this.dealType = dealType;
	}
	
	public boolean isSkipNull() {
		return skipNull;
	}
	
	public void setSkipNull(boolean skipNull) {
		this.skipNull = skipNull;
	}

	/**
	 * 处理异常消息
	 */
	protected void dealExceptionMsg(ConsumeBean<?, T> bean) {
		if(DealType.ACK.equals(dealType)) {
			adapter.ack(bean.getUniqueId());
//		} else if (DealType.REQUEUE.equals(dealType)) {
//			adapter.requeue(bean);
		} else if (DealType.NOTHING.equals(dealType)) {
			
		}
	}
	
	/**
	 * 重试消费逻辑
	 * @param value
	 * @return
	 * @throws InterruptedException
	 */
	protected boolean retry(V value) throws InterruptedException {
		int times = 1;
		while(times <= retryTimes) {
			Thread.sleep(times * times * 100);
			try {
				if(deal(value)) {
					return true;
				}
			} catch (RuntimeException e) {
				log.warn("消费数据异常：" + JSON.toJSONString(value), e);
			}
			times++;
		}
		return false;
	}
	
	protected boolean fillBeans() throws InterruptedException {
		long startMillis = System.currentTimeMillis();
		while(System.currentTimeMillis() - startMillis < readTimeoutMillis 
				&& cacheBeans.size() < maxPoll && !stop) {
			cacheBeans.addAll(adapter.poll(MIN_POLL_MILLIS));
		}
		if(skipNull && cacheBeans.size() == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 是否可以终止消费
	 * @return
	 */
	protected boolean canStop() {
		return stop && cacheBeans.size() == 0;
	}
	
	public boolean stop(long millis) throws InterruptedException {
		stop = true;
//		if(runThread != null && runThread.isAlive()) {// kafka不允许打断poll线程，异常：Failed to close coordinator	org.apache.kafka.common.errors.InterruptException: java.lang.InterruptedException	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.maybeThrowInterruptException(ConsumerNetworkClient.java:504) ~[kafka-clients-2.0.0.jar:?]
//			runThread.interrupt();
//			log.info("打断");
//		}
//		adapter.stop();// kafka不允许在poll之外的线程执行stop，异常：java.util.ConcurrentModificationException: KafkaConsumer is not safe for multi-threaded access	at org.apache.kafka.clients.consumer.KafkaConsumer.acquire(KafkaConsumer.java:2215) ~[kafka-clients-2.0.0.jar:?]
		countDown.await(millis, TimeUnit.MILLISECONDS);
		return true;
	}
	
	@Override
	public String toString() {
		return adapter.toString();
	}
	
	protected abstract boolean deal(V value);

}
