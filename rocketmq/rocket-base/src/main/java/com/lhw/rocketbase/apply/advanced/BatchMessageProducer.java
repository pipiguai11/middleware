package com.lhw.rocketbase.apply.advanced;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.*;

/**
 * @author ：linhw
 * @date ：21.11.1 16:29
 * @description：批量消息生产者
 *
 *      对于批量消息，它能显著提升传递小消息的性能，但是于此同时，它还存在一些限制，如下：
 *      1、这些消息都属于同一个topic
 *      2、相同的waitStoreMsgOK
 *      3、不能是延迟消息
 *      4、一批消息的大小总量不能超过4MB【这是重点】
 *          对于总量大小超过4MB的也有相应的处理方式，具体的可以看到后面的AdvancedBatchMessageProducer和ListSplitter两个内部类
 *
 *      要测试消费者消费消息，直接使用SimpleConsumer即可，监听的是同一个topic
 *
 * @modified By：
 */
public class BatchMessageProducer {

    public static void main(String[] args) {
//        sendSimpleMessage();
        sendAdvancedMessage();
    }

    /**
     * 发送简单的批量消息，总量大小不超过4MB
     */
    private static void sendSimpleMessage(){
        new Thread(new SimpleBatchMessageProducer()).start();
    }

    /**
     * 发送复杂的批量消息，总量大小超过了4MB
     */
    private static void sendAdvancedMessage(){
        new Thread(new AdvancedBatchMessageProducer()).start();
    }

    /**
     * 批量发送消息简单应用，消息总量大小不超过4MB的情况
     */
    private static class SimpleBatchMessageProducer extends AbstractProducer{

        public SimpleBatchMessageProducer(){
            super("SimpleDatchMessageProducer");
        }

        @SneakyThrows
        @Override
        public void runTask() {
            DefaultMQProducer producer = new DefaultMQProducer(getProducerName());
            producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
            producer.start();

            //构造简单批量消息
            String msg = SIMPLE_DATE_FORMAT.format(new Date()) + " hello_";
            List<Message> messages = new ArrayList<>();
            for (int i = 0 ; i < 5 ; i++){
                messages.add(new Message(getTopic(),(msg + i).getBytes()));
            }
            SendResult result = producer.send(messages);
            System.out.println(result);

            producer.shutdown();
        }

    }

    /**
     * 批量发送消息复杂情况，消息总量大小超过了4MB
     */
    private static class AdvancedBatchMessageProducer extends AbstractProducer{

        public AdvancedBatchMessageProducer(){
            super("AdvancedBatchMessageProducer");
        }

        @SneakyThrows
        @Override
        public void runTask() {
            DefaultMQProducer producer = new DefaultMQProducer(getProducerName());
            producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
            producer.start();

            //构造简单批量消息
            String msg = SIMPLE_DATE_FORMAT.format(new Date()) + " hello_";
            List<Message> messages = new ArrayList<>();
            for (int i = 0 ; i < 50 ; i++){
                messages.add(new Message(getTopic(),(msg + i).getBytes()));
            }

            //对消息进行分割，并发送
            ListSplitter splitter = new ListSplitter(messages);
            while (splitter.hasNext()){
                List<Message> temp = splitter.next();
                SendResult result = producer.send(temp);
                System.out.println(result);
            }

            producer.shutdown();
        }
    }

    /**
     * 内部类，用于分割消息，将一个大的消息分割成多个小的消息
     *      因为批量发送消息的限制是不允许超过4MB，因此需要将大于4MB的消息进行分割
     */
    private static class ListSplitter implements Iterator<List<Message>> {
        private final int SIZE_LIMIT = 1024 * 1024 * 4;
        private final List<Message> messages;
        private int currIndex;
        public ListSplitter(List<Message> messages) {
            this.messages = messages;
        }
        @Override public boolean hasNext() {
            return currIndex < messages.size();
        }
        @Override public List<Message> next() {
            int startIndex = getStartIndex();
            int nextIndex = startIndex;
            int totalSize = 0;
            for (; nextIndex < messages.size(); nextIndex++) {
                Message message = messages.get(nextIndex);
                int tmpSize = calcMessageSize(message);
                if (tmpSize + totalSize > SIZE_LIMIT) {
                    break;
                } else {
                    totalSize += tmpSize;
                }
            }
            List<Message> subList = messages.subList(startIndex, nextIndex);
            currIndex = nextIndex;
            return subList;
        }
        private int getStartIndex() {
            Message currMessage = messages.get(currIndex);
            int tmpSize = calcMessageSize(currMessage);
            while(tmpSize > SIZE_LIMIT) {
                currIndex += 1;
                Message message = messages.get(currIndex);
                tmpSize = calcMessageSize(message);
            }
            return currIndex;
        }
        private int calcMessageSize(Message message) {
            int tmpSize = message.getTopic().length() + message.getBody().length;
            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }
            tmpSize = tmpSize + 20; // 增加⽇日志的开销20字节
            return tmpSize;
        }
    }

}
