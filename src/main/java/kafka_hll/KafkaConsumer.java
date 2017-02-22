package kafka_hll;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Date May 22, 2015
 * @Author dengjie
 * @Note Kafka Consumer
 */
public class KafkaConsumer extends Thread {
    private final int SLEEP = 1000 * 3;
    private ConsumerConnector consumer;
    private String topic;

    public KafkaConsumer(String topic) {
        consumer = Consumer.createJavaConsumerConnector(this.consumerConfig());
        this.topic = topic;
    }

    private ConsumerConfig consumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", ConfigureAPI.KafkaProperties.ZK);
        props.put("group.id", ConfigureAPI.KafkaProperties.GROUP_ID);
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }

    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while ((boolean) it.hasNext()) {
            System.out.println("Receive->[" + new String(it.next().message()) + "]");
            try {
                sleep(SLEEP);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}