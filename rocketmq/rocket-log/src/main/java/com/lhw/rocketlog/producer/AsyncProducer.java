package com.lhw.rocketlog.producer;

import com.lhw.rocketlog.constant.BaseConstant;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;


/**
 * @author ：linhw
 * @date ：21.11.8 15:32
 * @description：异步发送消息生产者
 * @modified By：
 */
@Component(BaseConstant.Producer.MY_ASYNC_PRODUCER)
public class AsyncProducer extends AbstractLogProducer implements IProducer{

    public AsyncProducer() {
        super(AsyncProducer.class);
        //设置异步发送失败后重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);
    }

    @Override
    public void send(Message message) {
        try {
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("异步消息发送成功，主题：【" + message.getTopic() + "】");
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println(e.getMessage());
                    System.out.println("消息发送失败，出现异常");
                    // TODO : 这里需要做失败消息的处理，入库
                }
            });
        }catch (MQClientException | InterruptedException | RemotingException e){
            e.printStackTrace();
        }
    }

}
