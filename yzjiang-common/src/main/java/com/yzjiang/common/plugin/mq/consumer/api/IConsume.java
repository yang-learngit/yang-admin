package com.yzjiang.common.plugin.mq.consumer.api;

/**
 * 单条数据消费
 * 注意：必须保证线程安全，可能会被多线程调用consume
 * @Description 
 * 
 * @author liuzhao
 * @date 2018年10月5日 下午10:22:05 
 * @param <V>
 */
public interface IConsume<V> {
	/**
	 * 消费一条数据，如果抛出异常，会做指定次数的重试，依然异常则按照配置的处理处理策略执行，如丢弃、退回、不做ACK
	 * @param bean 可能为null
	 */
	public void consume(V bean);
}
