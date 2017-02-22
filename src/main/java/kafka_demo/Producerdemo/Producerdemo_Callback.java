package kafka_demo.Producerdemo; /**
 * Created by wujiao on 2016/12/16.
 * kafka 0.10.1.0
 * 测试Callback
 */

import kafka_demo.KafkaProperties;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class Producerdemo_Callback extends Thread {
    private final String topic;
    private final Properties props = new Properties();
    private Producer<String, String> producer;

    public Producerdemo_Callback(String topic) {
        props.put("bootstrap.servers", KafkaProperties.bootstrapservers);
        props.put("group.id", KafkaProperties.GROUP_ID);
        props.put("key.serializer", KafkaProperties.StringSerializer);
        props.put("value.serializer", KafkaProperties.StringSerializer);
        this.topic = topic;
        producer = new KafkaProducer(props);
    }

    @Override
    public void run() {
        ProducerRecord record = new ProducerRecord<String, String>(KafkaProperties.topic, "message", "测试callback函数");
        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null)
                    System.out.println("the producer has error:" + exception.getMessage());
                else {
                    System.out.println("producer_offset:" + metadata.offset());
                    System.out.println("producer_partition:" + metadata.partition());
                    System.out.println("producer_topic:" + metadata.topic());
                    System.out.println("producer_toString:" + metadata.toString());
                    System.out.println("producer_checksum:" + metadata.checksum());
                    System.out.println("producer_serializedKeySize:" + metadata.serializedKeySize());
                    System.out.println("producer_timetamp" + metadata.timestamp());
                }
            }

            ;
        });
        producer.flush();
        producer.close();


    }
}
