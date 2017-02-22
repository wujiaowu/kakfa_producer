package kafka_demo.Consumerdemo;

/**
 * Created by wujiao on 2017/2/8.
 * Kafka的Consumer的接口为非线程安全的。多线程共用IO，Consumer线程需要自己做好线程同步。
 * 如果想立即终止consumer，唯一办法是用调用接口：wakeup()，使处理线程产生WakeupException。
 */

import kafka_demo.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumerdemo_MultiThread implements Runnable {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final String topic;
    private final Properties props = new Properties();
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

    public Consumerdemo_MultiThread(String topic) {
        props.put("bootstrap.servers", KafkaProperties.bootstrapservers);
        props.put("group.id", KafkaProperties.GROUP_ID);
        props.put("key.deserializer", KafkaProperties.StringDeserializer);
        props.put("value.deserializer", KafkaProperties.StringDeserializer);
        this.topic = topic;
    }

    public void run() {
        try {
            consumer.subscribe(Arrays.asList(KafkaProperties.topic));
            while (!closed.get()) {
                ConsumerRecords records = consumer.poll(10000);
            }
        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }

    // Shutdown 这可以单独的线程调用
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}

