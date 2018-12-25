package com.yzjiang.common.plugin.mq.consumer.adapter;

import java.util.List;

import com.yzjiang.common.plugin.mq.consumer.bean.ConsumeBean;

/**
 * 各种MQ消费适配器接口
 * @Description 
 * 
 * @author liuzhao
 * @date 2018年10月5日 下午10:00:46 
 * @param <T>	消费的消息类型
 * @param <V>	ack参数类型
 */
public interface ConsumeAdapter<T> {
//	protected boolean stop = false;// 此处不使用volatile会不会提高性能
	
	/**
	 * 在指定时间内，获取一条数据
	 * @param millis	最大等待毫秒数
	 * @return
	 * 	返回值可能为null
	 */
//	<V> ConsumeBean<V, T> get(Long millis) throws InterruptedException;
	
	/**
	 * 指定时间内，获取一批数据
	 * @param millis	最大等待毫秒数
	 * @return
	 * 	返回值可能为null
	 */
	<V> List<ConsumeBean<V, T>> poll(Long millis) throws InterruptedException;
	
	/**
	 * 标记已成功消费的消息
	 * @param uniqueId	消息唯一标识，不同MQ该值类型不一样
	 */
	void ack(T uniqueId);
	
	/**
	 * 将消息加入队列尾部，重新消费，可能出现循环消费的风险，暂不实现
	 * @param value
	 */
//	public <V> void requeue(ConsumeBean<V> value);
	
	/**
	 * 停止订阅
	 */
//	public void stop() {
//		stop = true;
//	}
	
	/**
	 * 关闭资源
	 */
	void close();
}
