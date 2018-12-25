package com.yzjiang.common.plugin.mq.consumer.bind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import com.yzjiang.common.plugin.mq.consumer.adapter.ConsumeAdapter;
import com.yzjiang.common.plugin.mq.consumer.api.IConsume;
import com.yzjiang.common.plugin.mq.consumer.bean.ConsumeBean;

public class ConsumeBind<V, T> extends ConsumeBindAbs<V, T> {
	private static Logger log = LoggerFactory.getLogger(ConsumeBind.class);
	private IConsume<V> consume;
	
	public ConsumeBind(ConsumeAdapter<T> adapter, IConsume<V> consume) {
		super(adapter, 1);
		this.consume = consume;
	}
	
	@Override
	public void run() {
		try {
//			runThread = Thread.currentThread();
			while (!canStop()) {
				if(!fillBeans()) {
					continue;
				}
				ConsumeBean<V, T> value = cacheBeans.size() > 0 ? cacheBeans.remove(0) : null;
				boolean success = false;
				try {
					success = deal(value == null ? null : value.getValue());
				} catch (Exception e) {
					log.warn("消费数据异常：" + JSON.toJSONString(value), e);
				}
				if(value == null) {
					continue;
				}
				if(!success) {
					success = retry(value.getValue());
				}
				if(success) {
					adapter.ack(value.getUniqueId());
				} else {
					dealExceptionMsg(value);
					log.warn("【" + dealType + "】消息重试处理异常：" + JSON.toJSONString(value));
				}
			}
		} catch (InterruptedException e) {
			log.info("消费被打断，已停止消费消息");
		} catch (Exception e) {
			log.error("消费异常，已停止消费消息", e);
		} finally {
			try {
				adapter.close();
			} catch (Exception e) {
				log.warn("停止消费:" + e.getMessage());
			}
			countDown.countDown();
		}
	}
	
	protected boolean deal(V value) {
		consume.consume(value);
		return true;
	}
	
}
