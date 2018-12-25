package com.yzjiang.common.plugin.mq.consumer.api;

import java.util.List;

/**
 * 批量消费数据
 * 注意：必须保证线程安全，可能会被多线程调用consume
 * @Description 
 * 
 * @author liuzhao
 * @date 2018年10月5日 下午10:22:20 
 * @param <V>
 */
public interface IBatchConsume<V> {
	/**
	 * 批量消费一组数据，执行完成后，可抛出异常，如果抛出异常会对该批所有数据做指定次数的重试，如果依然异常，会按照配置做处理
	 * @param beans	不一定包含设定条数的数据，可能为长度为0
	 * @return
	 * 	返回处理异常的数据，异常数据会按照配置做处理，如果处理成功则返回null，不可以返回空集合
	 */
	public List<V> consume(List<V> beans);
}
