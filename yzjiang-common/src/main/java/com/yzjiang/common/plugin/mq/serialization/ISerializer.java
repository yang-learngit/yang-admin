package com.yzjiang.common.plugin.mq.serialization;

public interface ISerializer<T> {
	/**
	 * 将字节序列化为对象
	 * @param data
	 * @return
	 */
	T deserialize(byte[] data);
	
	/**
	 * 将对象序列化为字节
	 * @param data
	 * @return
	 */
	byte[] serialize(T data);
}
