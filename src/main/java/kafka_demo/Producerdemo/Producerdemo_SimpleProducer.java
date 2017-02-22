package kafka_demo.Producerdemo; /**
 * Created by wujiao on 2016/12/16.
 * kafka 0.10.1.0
 * 测试SimpleProducer+详细解释
 */

import kafka_demo.KafkaProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producerdemo_SimpleProducer extends Thread {
    private final String topic;
    private final Properties props = new Properties();
    private Producer<String, String> producer;

    public Producerdemo_SimpleProducer(String topic) {
        //初始集群，有多少主机及端口，可动态更改，端口为kafka-broker，于server.properties配置。
        props.put("bootstrap.servers", KafkaProperties.bootstrapservers);
        //consumergroup，同一个group只有一个consumer可订阅消息，多个group可同时消费一个topic
        props.put("group.id", KafkaProperties.GROUP_ID);
        // 将ProducerRecord 从 String 转换为 byte,设置序列化类，配置key.serializer,value.serializer
        props.put("key.serializer", KafkaProperties.StringSerializer);
        props.put("value.serializer", KafkaProperties.StringSerializer);

        this.topic = topic;
        producer = new KafkaProducer(props);
    }

    @Override
    public void run() {
       /* 一条record通常包括5个字段：topic，partition，key，value，timestamp
        1.如果指定了某个分区，会只将消息发送到这个分区上。
        2.如果同时指定了某个分区的key，则也会将消息发送到指定分区上，key不起作用。
        3.如果没有指定分区的key,那么将会随机发送到topic的分区中。
        4.如果指定了key，那么将会以hash<key>的方式发送到分区中。*/
        ProducerRecord record = new ProducerRecord<String, String>(KafkaProperties.topic, 0, "message", "简单producer");
        // send(ProducerRecord<K,V> record)
        producer.send(record);
        //Return a map of metrics maintained by the producer         
        //System.out.println(producer.metrics().toString());
        //列出指定topic所在分区的信息:topic,partition,leader,replicas,isr
        System.out.println(producer.partitionsFor(KafkaProperties.topic));
        //立即发送累积在缓冲区的消息
        producer.flush();
        //关闭producer
        producer.close();
    }
}
