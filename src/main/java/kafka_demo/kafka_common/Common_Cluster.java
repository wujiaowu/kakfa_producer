package kafka_demo.kafka_common;

/**
 * Created by wujiao on 2017/1/16.
 * MetricName class
 * metric用处?
 */


import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.metrics.MetricConfig;
import org.apache.kafka.common.metrics.Metrics;
import org.apache.kafka.common.metrics.Sensor;
import org.apache.kafka.common.metrics.stats.Avg;
import org.apache.kafka.common.metrics.stats.Max;
import org.apache.kafka.common.metrics.stats.Min;

import java.util.LinkedHashMap;
import java.util.Map;

public class Common_Cluster extends Thread {

    @Override
    public void run() {
        Map<String, String> metricTags = new LinkedHashMap<String, String>();
        metricTags.put("client-id", "producer-1");
        metricTags.put("topic", "topic");

        MetricConfig metricConfig = new MetricConfig().tags(metricTags);
        Metrics metrics = new Metrics(metricConfig); // this is the global repository of metrics and sensors

        Sensor sensor = metrics.sensor("message-sizes");

        MetricName metricName = metrics.metricName("message-size-avg", "producer-metrics", "average message size");
        sensor.add(metricName, new Avg());

        metricName = metrics.metricName("message-size-max", "producer-metrics");
        sensor.add(metricName, new Max());

        metricName = metrics.metricName("message-size-min", "producer-metrics", "message minimum size", "client-id", "my-client", "topic", "my-topic");
        sensor.add(metricName, new Min());

        // as messages are sent we record the sizes
        sensor.record();


        //node
        Node node = new Node(2, "127.0.0.1", 9093);
        System.out.println(node.toString());
    }
}
