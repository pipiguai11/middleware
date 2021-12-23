package com.lhw.rocketbase.apply.simple.producer;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
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
public class AsyncSendMessageProducer extends AbstractProducer{

    public AsyncSendMessageProducer(){
        super("AsyncSendMessageProducer", Constant.Topic.ASYNC_TOPIC,Constant.Tag.TAG_B);
    }

    @SneakyThrows
    @Override
    public void runTask() {
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
                msg = new Message(getTopic(),getTag(),
                        ("生产者发送消息" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            }else {
                //为了测试onException，放一个找不到的topic上去
                msg = new Message(getTopic() + "_1",getTag(),
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
    }

    public static void main(String[] args) {
        new Thread(new AsyncSendMessageProducer()).start();
    }

}
