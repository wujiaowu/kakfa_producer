package kafka_demo.Consumerdemo;
/**
 * Created by wujiao on 2016/12/16.
 * 定位到指定分区，指定偏移量开始消费
 * 用处：解决重复消费问题；重复消费消费过的消息
 **/
/*
insertIntoDb(buffer);
kafka的集群保存committed offset,但不能避免重复消费，如果在数据处理完成，commitSync的时候挂了，服务器再次重启，消息仍然会重复消费。
例如：consumer获得数据后，需要将数据持久化到DB中。自动确认offset的情况下，如果数据从kafka集群读出，就确认，但是持久化过程失败，就会导致数据丢失。我们就需要控制offset的确认。
解决重复消费的两种方法（自己保存offset）：
1.关系数据库，通过事务存取。
2.搜索引擎，把offset作为索引。
在每次取到消息后将对应的offset存储，在consumer failed，重启时，通过consumer.seek函数，定位到保存的offset,开始消费。
*/

import kafka_demo.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;


public class Consumerdemo_ManualOffset extends Thread {
    private final String topic;
    private final Properties props = new Properties();

    public Consumerdemo_ManualOffset(String topic) {
        props.put("bootstrap.servers", KafkaProperties.bootstrapservers);
        props.put("group.id", KafkaProperties.GROUP_ID);
        props.put("key.deserializer", KafkaProperties.StringDeserializer);
        props.put("value.deserializer", KafkaProperties.StringDeserializer);
        this.topic = topic;
    }

    @Override
    public void run() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        while (true) {
            final Collection<TopicPartition> partition = new Collection<TopicPartition>() {
                public int size() {
                    return 0;
                }

                public boolean isEmpty() {
                    return false;
                }

                public boolean contains(Object o) {
                    return false;
                }

                public Iterator<TopicPartition> iterator() {
                    return null;
                }

                public Object[] toArray() {
                    return new Object[0];
                }

                public <T> T[] toArray(T[] a) {
                    return null;
                }

                public boolean add(TopicPartition topicPartition) {
                    return false;
                }

                public boolean remove(Object o) {
                    return false;
                }

                public boolean containsAll(Collection<?> c) {
                    return false;
                }

                public boolean addAll(Collection<? extends TopicPartition> c) {
                    return false;
                }

                public boolean removeAll(Collection<?> c) {
                    return false;
                }

                public boolean retainAll(Collection<?> c) {
                    return false;
                }

                public void clear() {

                }
            };
            TopicPartition topicPartition = new TopicPartition(KafkaProperties.topic, 0);
/*
            error:java.lang.IllegalStateException: No current assignment for partition test-0
            原因：手动指定消费位置，无法自动负载均衡，所以要手动注册，才能消费
            使用consumer.assign(Arrays.asList(topicPartition));
            error:java.lang.IllegalStateException: Subscription to topics, partitions and pattern are mutually exclusive
            原因：subscribe和assign只能使用一个
            删去consumer.subscribe(Arrays.asList(KafkaProperties.topic));
*/
            consumer.assign(Arrays.asList(topicPartition));
/*
            将offset保存，每次消费时指定offset, 避免重复消费
            设置从指定的位置开始消费
            consumer.seek(topicPartition, 90);
            partition.add(topicPartition);
            consumer.seekToBeginning(partition);
            consumer.seekToEnd(partition);   
*/
            consumer.seek(topicPartition, 100);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                for (ConsumerRecord<String, String> record : records)
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }


        }
    }
}

