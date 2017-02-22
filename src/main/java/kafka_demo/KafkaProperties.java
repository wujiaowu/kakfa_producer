package kafka_demo;

/**
 * Created by wujiao on 2016/12/16.
 */

public interface KafkaProperties {
    //        final static String bootstrapservers = "16.173.244.104:9097,16.173.244.106:9098,16.173.246.77:9099";
//        final static String topic = "wj-topic";
    String bootstrapservers = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094";
    String topic = "test";
    String GROUP_ID = "wj";
    String CLIENT_ID = "TEST1";
    String StringSerializer = "org.apache.kafka.common.serialization.StringSerializer";
    String StringDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
    int kafkaProducerBufferSize = 64 * 1024;
    int connectionTimeOut = 20000;
    int reconnectInterval = 10000;
}
