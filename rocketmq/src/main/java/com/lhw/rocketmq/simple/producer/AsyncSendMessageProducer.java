package com.lhw.rocketmq.simple.producer;

import com.lhw.rocketmq.base.Constant;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author ：linhw
 * @date ：21.10.29 14:55
 * @description：异步发送消息生产者
 * @modified By：
 */
public class AsyncSendMessageProducer extends AbstractProducer implements Runnable {

    private final String topic = "AsyncTopic";

    private final String tag = "TagB";

    public AsyncSendMessageProducer(){
        setProducerName("AsyncSendMessageProducer");
    }

    @SneakyThrows
    @Override
    public void run() {
        DefaultMQProducer producer = new DefaultMQProducer(getProducerName());
        producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
        producer.start();
        //设置异步发送失败后重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);
        System.out.println("生产者启动！");
        int i = 0;
        while (i < 10){
            //构建消息对象
            Message msg;
            if (i < 6){
                msg = new Message(topic,tag,
                        ("生产者发送消息" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            }else {
                //为了测试onException，放一个找不到的topic上去
                msg = new Message(topic + "_1",tag,
                        ("生产者发送消息" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            }
            int finalI = i;
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("生产者成功发送消息，当前个数为：【" + finalI + "】");
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println("出现异常，发送失败:" + e.getMessage());
                }
            });

            i++;
            Thread.sleep(2000);
        }
        producer.shutdown();
        listener.finsh(msg -> System.out.println("【" + msg + "】异步消息生产者停止生产消息了"));
    }

    public static void main(String[] args) {
        new Thread(new AsyncSendMessageProducer()).start();
    }

}
