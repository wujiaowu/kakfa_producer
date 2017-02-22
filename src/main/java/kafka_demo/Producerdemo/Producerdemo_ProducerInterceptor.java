package kafka_demo.Producerdemo; /**
 * Created by wujiao on 2016/12/16.
 * kafka 0.10.1.0
 * 测试ProducerInterceptor
 * 用处：拦截器接口，可以自定义自己的拦截器实现，有以下两个method：
 * onSend: KafkaProducer：send 调用时就会执行此方法。
 * onAcknowledgement：发送失败，或者发送成功（broker 通知producer代表发送成功）时都会调用该方法。
 * 用法 props.put("interceptor.classes", .class);拦截器类的列表
 */

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class Producerdemo_ProducerInterceptor implements ProducerInterceptor {

    public ProducerRecord onSend(ProducerRecord record) {
        return null;
    }

    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    public void close() {

    }

    public void configure(Map<String, ?> configs) {

    }

}
