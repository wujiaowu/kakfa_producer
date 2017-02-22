package kafka_demo.Producerdemo;
/**
 * Created by wujiao on 2016/12/16.
 * kafka 0.10.1.0
 * 测试Partitioner分区接口:Compute the partition for the given record。
 * 用处：以实现自定义的消息分区，这个设置决定了将消息发送给哪一个分区。
 * 如果用户创建record时，没有指定partition属性。则由partition计算工具（Partitioner 接口）来计算出partition。
 * 如果提供了Key值，会根据key序列化后的字节数组的hashcode进行取模运算。
 * 如果没有提供key，则采用迭代方式（取到的值并非完美的迭代，而是类似于随机数）。
 * 好处：如果partition规则设置的合理，所有消息可以均匀分布到不同的partition里，这样就实现了水平扩展。
 * （如果一个topic对应一个文件，那这个文件所在的机器I/O将会成为这个topic的性能瓶颈，而partition解决了这个问题）。
 * 用法：使用方法在producer类中配置props.put("partitioner.class", "Producerdemo.Producerdemo_Partitioner.class");
 * 注意：要先用命令创建topic及partitions 分区数;否则在自定义的分区中如果有大于1的情况下，发送数据消息到kafka时会报expired due to timeout while requesting metadata from brokers错误
 **/

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class Producerdemo_Partitioner implements Partitioner {
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numpartition = partitions.size();
        int partitionnum = 0;
        try {
            /*
            默认情况下，kafka根据传递消息的key来进行分区的分配，保证了相同key的消息被分到相同的分区
*/
            partitionnum = Integer.parseInt((String) key);
        } catch (Exception e) {
            partitionnum = key.hashCode();
        }
        System.out.print("分区" + partitionnum % numpartition);
        return Math.abs(partitionnum % numpartition);
    }

    public void close() {

    }

    public void configure(Map<String, ?> configs) {

    }
}