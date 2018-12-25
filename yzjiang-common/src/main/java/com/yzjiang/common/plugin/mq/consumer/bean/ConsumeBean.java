package com.yzjiang.common.plugin.mq.consumer.bean;

public class ConsumeBean<V, T> {
	private V value;
	private T uniqueId;
	
	public ConsumeBean(V value, T uniqueId) {
		this.value = value;
		this.uniqueId = uniqueId;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public T getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(T uniqueId) {
		this.uniqueId = uniqueId;
	}
	
}
