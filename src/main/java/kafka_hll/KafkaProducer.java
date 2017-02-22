package kafka_hll;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * @Date May 22, 2015
 * @Author dengjie
 * @Note Kafka JProducer
 */
public class KafkaProducer extends Thread {
    private Producer<Integer, String> producer;
    private String topic;
    private Properties props = new Properties();


    public KafkaProducer(String topic) {
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "127.0.0.1:9092");
        producer = new Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
    }

    @Override
    public void run() {
        int offsetNo = 1;
        while (offsetNo < 10) {
            String msg = new String("Message_" + offsetNo);
            System.out.println("Send->[" + msg + "]");
            producer.send(new KeyedMessage<Integer, String>(topic, msg));
            offsetNo++;
        }
    }
}


