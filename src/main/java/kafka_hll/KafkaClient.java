package kafka_hll;

/**
 * @Date May 22, 2015
 * @Author dengjie
 * @Note To run Kafka Code
 */
public class KafkaClient {

    public static void main(String[] args) {
        KafkaProducer pro = new KafkaProducer(ConfigureAPI.KafkaProperties.TOPIC);
        pro.start();

        KafkaConsumer con = new KafkaConsumer(ConfigureAPI.KafkaProperties.TOPIC);
        con.start();
    }
}