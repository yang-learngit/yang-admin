package com.yzjiang.common.plugin.mq.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsOptions;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;

import com.yzjiang.common.plugin.mq.util.Commons;

/**
 * @author Administrator
 */
public class KafkaService {
	
	/**
	 * 创建主题忽略重复创建异常
	 * @param servers
	 * @param topic
	 * @param numPartitions
	 * @param replicationFactor
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 */
	public static void createTopicIgnoreExists(String servers, String topic, int numPartitions, short replicationFactor) throws InterruptedException, ExecutionException {
		try {
			createTopic(servers, topic, numPartitions, replicationFactor);
		} catch (ExecutionException e) {
			if(!Commons.existThrowable(e, TopicExistsException.class)) {
				throw e;
			}
		}
	}
	
	/**
	 * 创建主题
	 * @param servers
	 * @param topic
	 * @param numPartitions
	 * @param replicationFactor
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void createTopic(String servers, String topic, int numPartitions, short replicationFactor) throws InterruptedException, ExecutionException {
		AdminClient adminClient = null;
		try {
			adminClient = getAdminClient(servers);
			ArrayList<NewTopic> topics = new ArrayList<NewTopic>();
			NewTopic newTopic = new NewTopic(topic, numPartitions, replicationFactor);
			topics.add(newTopic);
			CreateTopicsResult result = adminClient.createTopics(topics);
			result.all().get();
		} finally {
			if(adminClient != null) {
				adminClient.close();
			}
		}
	}
	
	/**
	 * 检查某个主题是否存在
	 * @param servers
	 * @param topic
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static boolean existsTopic(String servers, String topic) throws InterruptedException, ExecutionException {
		AdminClient adminClient = null;
		try {
			adminClient = getAdminClient(servers);
			ListTopicsResult result = adminClient.listTopics();
			Set<String> names = result.names().get();
			return names.contains(topic);
		} finally {
			if(adminClient != null) {
				adminClient.close();
			}
		}
	}
	
	/**
	 * 删除主题，仅为标记删除（不可再次使用），kafka节点配置需增加：delete.topic.enable=true
	 * 
	 * 注意：删除后可能导致节点退出，目前在windows上出现节点退出问题，异常为：java.nio.file.AccessDeniedException: E:\tmp\kafka-logs-1\test_topic_08-0 -> E:\tmp\kafka-logs-1\test_topic_08-0.e651689fe93a45deb28f81a0234605b6-delete
	 * 如果需要彻底删除，需额外做如下步骤
	 * 	1.删除kafka节点目录中对应topic的目录，如：E:\tmp\kafka-logs\topic-1
	 * 	2.进入zookeeper命令行，删除zookeeper对应的节点：
	 * 		rmr /brokers/topics/topic-1
	 *		rmr /admin/delete_topics/topic-1
	 *	3.重启所有节点，重启zookeeper
	 *	
	 *	如果不做第三步，再次使用删除的topic，创建主题时会卡很长时间，消费时会出现LEADER_NOT_AVAILABLE异常，通过查看topic描述，也是无leader的
	 * @param servers
	 * @param topic
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void deleteTopic(String servers, String[] topic) throws InterruptedException, ExecutionException {
		AdminClient adminClient = null;
		try {
			adminClient = getAdminClient(servers);
			DeleteTopicsOptions options = new DeleteTopicsOptions();
			DeleteTopicsResult result = adminClient.deleteTopics(Arrays.asList(topic), options);
			result.all().get();
		} finally {
			if(adminClient != null) {
				adminClient.close();
			}
		}
	}
	
	private static AdminClient getAdminClient(String servers) {
		Properties props = new Properties();
		props.put("bootstrap.servers", servers);
		return AdminClient.create(props);
	}
}
