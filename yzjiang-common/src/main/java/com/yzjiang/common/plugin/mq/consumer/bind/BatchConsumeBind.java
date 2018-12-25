package com.yzjiang.common.plugin.mq.consumer.bind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import com.yzjiang.common.plugin.mq.consumer.adapter.ConsumeAdapter;
import com.yzjiang.common.plugin.mq.consumer.api.IBatchConsume;
import com.yzjiang.common.plugin.mq.consumer.bean.ConsumeBean;

public class BatchConsumeBind<V, T> extends ConsumeBindAbs<V, T> {
	private static Logger log = LoggerFactory.getLogger(BatchConsumeBind.class);
	private int batchSize;
	private IBatchConsume<V> consumer;
	
	public BatchConsumeBind(ConsumeAdapter<T> adapter, IBatchConsume<V> consumer, int batchSize) {
		super(adapter, batchSize);
		this.consumer = consumer;
		this.batchSize = batchSize;
	}
	
	@Override
	public void run() {
		try {
//			runThread = Thread.currentThread();
			while (!canStop()) {
				if(!fillBeans()) {
					continue;
				}
				int consumeSize = cacheBeans.size() > batchSize ? batchSize : cacheBeans.size();
				List<ConsumeBean<V, T>> batchBeans = new ArrayList<>(consumeSize);
				List<V> unConsumeBeans = new ArrayList<>(consumeSize);
				while(batchBeans.size() < batchSize && cacheBeans.size() > 0) {
					ConsumeBean<V, T> bean = cacheBeans.remove(0);
					batchBeans.add(bean);
					unConsumeBeans.add(bean.getValue());
				}
				try {
					unConsumeBeans = consumer.consume(unConsumeBeans);
				} catch (Exception e) {
					log.warn("消费数据异常：" + JSON.toJSONString(unConsumeBeans), e);
				}
				if(consumeSize == 0) {
					continue;
				}
				if(unConsumeBeans != null && unConsumeBeans.size() > 0 && retryTimes > 0) {
					Iterator<V> ite = unConsumeBeans.iterator();
					while(ite.hasNext()) {
						V value = ite.next();
						if(retry(value)) {
							ite.remove();
						} else {
							log.warn("【" + dealType + "】消息重试处理异常：" + JSON.toJSONString(value));
						}
					}
				}
				for(ConsumeBean<V, T> bean : batchBeans) {
					if(unConsumeBeans != null && unConsumeBeans.contains(bean.getValue())) {
						dealExceptionMsg(bean);
					} else {
						adapter.ack(bean.getUniqueId());
					}
				}
			}
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
	
	@Override
	protected boolean deal(V value) {
		List<V> list = new ArrayList<>();
		list.add(value);
		List<V> errorBean = consumer.consume(list);
		return errorBean == null || errorBean.size() == 0;
	}
	
}
