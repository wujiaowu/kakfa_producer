package kafka_demo.Consumerdemo; /**
 * Created by wujiao on 2016/12/16.
 * 测试批量提交消息确认(手动同步ack)
 * 用处：当希望获取消息并对消息进行一定逻辑处理后，才认为该消息已经被消费时。
 * buffer达到指定大小时，返回消息确认，在此之前，均从旧的offset处开始拉取数据。
 **/

/*
重复消费问题：
“消费确认”：拿到消息之后，处理完毕，向消息中间件发送ack，或者说confirm。
2个offset值： 一个是当前取消息所在的consume offset，一个是处理完毕，发送ack之后所确定的committed offset。
在异步模式下，committed offset要落后于consume offset。
假如consumer挂了重启，那它将从committed offset位置开始重新消费，而不是consume offset位置。这也就意味着有可能重复消费。

机制:
acks=0: producer将不会等待来自服务器的确认。记录将被立即添加到缓冲区并认为发送。不能做任何保证，每个记录的偏移将始终被设置为-1。
acks=1: 等待leader将记录到本地日志，不会等待确认。
acks=all或acks=-1:leader需要等待全部副本确认，保证了消息不会丢失

策略：
    1.自动周期性的ACK 
        props.put("enable.auto.commit","true");
        props.put("auto.commit.interval.ms","1000");
    2.consumer.commitSync()
        手动同步ack，每处理完一条消息，同步提交一次
    3.consumer.commitASync()
        手动异步ack
*/

import kafka_demo.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Consumerdemo_ManualCommit extends Thread {
    private final String topic;
    private final Properties props = new Properties();

    public Consumerdemo_ManualCommit(String topic) {
        props.put("bootstrap.servers", KafkaProperties.bootstrapservers);
        props.put("group.id", KafkaProperties.GROUP_ID);

        //禁止自动提交
        props.put("enable.auto.commit", "false");
        /*
        当Kafka中没有初始偏移或如果当前偏移在服务器上不再存在时（例如，因为该数据已被删除），这个设置才有效
        earliest：自动将偏移重置为最早偏移。latest：自动将偏移重置为最新的偏移。none：如果没有为消费者的组找到任何以前的偏移，向消费者抛出异常
        默认为latest
        */
        props.put("auto.offset.reset", "latest");

        props.put("key.deserializer", KafkaProperties.StringDeserializer);
        props.put("value.deserializer", KafkaProperties.StringDeserializer);
        this.topic = topic;
    }

    @Override
    public void run() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(KafkaProperties.topic));
        //批量提交
        final int minBatchSize = 20;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<ConsumerRecord<String, String>>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
            //若一直未同步offset，每次consumer拉取数据将会从旧的offset处开始拉取数据到最新的offset，重复拉取旧的数据
            if (buffer.size() >= minBatchSize) {
                //更新数据库的offset，之后开始从新的offset处获取数据
                consumer.commitSync();
                buffer.clear();
            }
        }
    }
}


