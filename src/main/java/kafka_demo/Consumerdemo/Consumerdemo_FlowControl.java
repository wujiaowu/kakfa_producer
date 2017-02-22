package kafka_demo.Consumerdemo;

import kafka_demo.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by wujiao on 2017/2/8.
 * consumer同时消费多个分区
 * 让某个分区消费先暂停，时机到了再恢复，然后接着poll。
 * 优先消费其他topic
 * pause(TopicPartition…)，resume(TopicPartition…)
 */
public class Consumerdemo_FlowControl extends Thread {
    private final String topic;
    private final Properties props = new Properties();

    public Consumerdemo_FlowControl(String topic) {
        props.put("bootstrap.servers", KafkaProperties.bootstrapservers);
        props.put("group.id", KafkaProperties.GROUP_ID);
        props.put("key.deserializer", KafkaProperties.StringDeserializer);
        props.put("value.deserializer", KafkaProperties.StringDeserializer);
        this.topic = topic;
    }

    @Override
    public void run() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(KafkaProperties.topic), new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                for (TopicPartition partition : partitions) {
                    System.out.printf("Revoked partition for client %s : %s-%s %n", KafkaProperties.CLIENT_ID, partition.topic(), partition.partition());
                }
            }

            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                for (TopicPartition partition : partitions) {
                    System.out.printf("Assigned partition for client %s : %s-%s %n", KafkaProperties.CLIENT_ID, partition.topic(), partition.partition());
                }
            }
        });
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            consumer.pause(Arrays.asList(new TopicPartition(topic, 0)));
            consumer.pause(Arrays.asList(new TopicPartition(topic, 1)));
            consumer.resume(Arrays.asList(new TopicPartition(topic, 0)));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }
}
