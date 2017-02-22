package kafka_demo.Consumerdemo; /**
 * Created by wujiao on 2016/12/16.
 * 简单Consumer例子
 **/ 
/*
向特定的分区拉取数据
TopicPartition partition0=new TopicPartition(topic,0);
TopicPartition partition1=new TopicPartition(topic,1);
consumer.assign(Arrays.asList(partition0,partition1));
*/

import kafka_demo.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class Consumerdemo_SimpleConsumer extends Thread {
    private final String topic;
    private final Properties props = new Properties();

    public Consumerdemo_SimpleConsumer(String topic) {
        props.put("bootstrap.servers", KafkaProperties.bootstrapservers);
        props.put("group.id", KafkaProperties.GROUP_ID);
        /*
        consumer定期提交offset
        props.put("enable.auto.commit", "true");
        提交的频率
        props.put("auto.commit.interval.ms", "1000");
        超过这个时间没有返回heartbeats，认为这个consumer dead，它的分区自动重新分配
        zookeeper.sync.time.ms时间设置过短就会导致old consumer还没有来得及释放资源，new consumer重试失败多次到达阀值就退出了。
        props.put("zookeeper.sync.time.ms", "2000");
        */
        props.put("key.deserializer", KafkaProperties.StringDeserializer);
        props.put("value.deserializer", KafkaProperties.StringDeserializer);
        this.topic = topic;
    }

    @Override
    public void run() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //订阅的topic列表
        consumer.subscribe(Arrays.asList(KafkaProperties.topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }
}

