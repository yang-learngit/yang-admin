package com.yzjiang.common.plugin.mq.util;

public class Commons {

	/**
	 * 检测参数是否合法，非法时抛出异常
	 * 
	 * @param expression
	 *            为true时抛出异常
	 * @param errorMessageTemplate
	 * @param errorMessageArgs
	 */
	public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
		if (expression) {
			throw new IllegalArgumentException(String.format(errorMessageTemplate, errorMessageArgs));
		}
	}

	/**
	 * 非法状态检测
	 * 
	 * @param expression
	 *            为true时抛出异常
	 * @param errorMessageTemplate
	 * @param errorMessageArgs
	 */
	public static void checkStatus(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
		if (expression) {
			throw new IllegalStateException(String.format(errorMessageTemplate, errorMessageArgs));
		}
	}
	
	public static String getShardQueue(String queue, int shardNum) {
		return getShardTopic(queue, shardNum);
	}
	
	public static String getShardTopic(String topic, int shardNum) {
		if(topic == null) {
			return null;
		}
		if(shardNum > 0) {
			return topic + "_" + shardNum;
		} else {
			return topic;
		}
	}
	
	/**
	 * 检查异常堆栈中是否指定异常类
	 * @param throwable
	 * @param cls
	 * @return
	 */
	public static boolean existThrowable(Throwable throwable, Class<? extends Throwable> cls) {
		if(throwable == null || cls == null) {
			return false;
		}
		while(throwable != null) {
			if(throwable.getClass().equals(cls)) {
				return true;
			}
			throwable = throwable.getCause();
		}
		return false;
	}
}
