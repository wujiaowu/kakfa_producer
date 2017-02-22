package kafka_demo;

import kafka_demo.Consumerdemo.Consumerdemo_FlowControl;
import kafka_demo.Producerdemo.Producerdemo_SimpleProducer;

/**
 * Created by wujiao on 2016/12/16.
 */
public class KafkaConsumerProducer {
    public static void main(String[] args) {
        Producerdemo_SimpleProducer producerThread = new Producerdemo_SimpleProducer(KafkaProperties.topic);
        producerThread.setName("producer线程");
        producerThread.start();
        Consumerdemo_FlowControl consumerThread = new Consumerdemo_FlowControl(KafkaProperties.topic);
        consumerThread.setName("consumer线程");
        consumerThread.start();
    }
}
