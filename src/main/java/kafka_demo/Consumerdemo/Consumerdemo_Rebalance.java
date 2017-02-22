package kafka_demo.Consumerdemo;

/**
 * Created by wujiao on 2017/2/13.
 * ConsumerRebalanceListener()
 */

import kafka_demo.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class Consumerdemo_Rebalance extends Thread {
    private final String topic;
    private final Properties props = new Properties();

    public Consumerdemo_Rebalance(String topic) {
        props.put("bootstrap.servers", KafkaProperties.bootstrapservers);
        props.put("group.id", KafkaProperties.GROUP_ID);
        props.put("client.id", KafkaProperties.CLIENT_ID);
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
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
            ;
        }
    }
}
