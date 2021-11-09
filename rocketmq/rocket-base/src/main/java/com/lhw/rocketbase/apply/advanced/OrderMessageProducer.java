package com.lhw.rocketbase.apply.advanced;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：linhw
 * @date ：21.11.1 11:12
 * @description：顺序消息生产者
 * @modified By：
 */
public class OrderMessageProducer extends AbstractProducer implements Runnable {

    public OrderMessageProducer(){
        super("orderMessageProducer");
    }

    public static void main(String[] args) {
        new Thread(new OrderMessageProducer()).start();
    }

    @SneakyThrows
    @Override
    public void run() {
        DefaultMQProducer producer = new DefaultMQProducer(getProducerName());
        producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
        producer.start();
        String[] tags = {Constant.Tag.TAG_A, Constant.Tag.TAG_C, Constant.Tag.TAG_D};
        List<OrderStep> orderSteps = initOrder();

        for (int i = 0 ; i < orderSteps.size() ; i++){
            String msg = SIMPLE_DATE_FORMAT.format(new Date()) + "Rocket Hello " + orderSteps.get(i);
            //构建消息
            Message message = new Message(getTopic(),
                    tags[i % tags.length],
                    "KEY"+i,
                    msg.getBytes());
            //发送消息，同时指定消息队列选择器，以及根据什么属性【orderId】进行选择
            SendResult result = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    //获取到传入的orderId值，这里的arg其实就是producer.send方法的第三个参数【如下的orderSteps.get(i).getOrderId()】
                    long orderId = (long)arg;
                    //通过计算选择对应的消息队列，因为订单的orderId是固定的，因此选择的队列肯定也是固定的
                    int index = (int)orderId % mqs.size();
                    return mqs.get(index);
                }
            },orderSteps.get(i).getOrderId());

            System.out.println(String.format("SendResult status : %s , queueId : %d , body : %s",
                    result.getSendStatus(),result.getMessageQueue().getQueueId(),msg));
        }
        producer.shutdown();
        finish();
    }


    private List<OrderStep> initOrder(){
        List<OrderStep> orders = new ArrayList<>();

        orders.add(new OrderStep.Builder()
                .setOrderId(10000300301L)
                .setDesc("创建订单1")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300302L)
                .setDesc("创建订单2")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300301L)
                .setDesc("支付订单1")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300303L)
                .setDesc("创建订单3")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300302L)
                .setDesc("支付订单2")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300303L)
                .setDesc("支付订单3")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300301L)
                .setDesc("完成订单1")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300302L)
                .setDesc("完成订单2")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300303L)
                .setDesc("推送订单3")
                .setCreateDate(new Date())
                .build());
        orders.add(new OrderStep.Builder()
                .setOrderId(10000300303L)
                .setDesc("完成订单3")
                .setCreateDate(new Date())
                .build());

        return orders;
    }

    /**
     * 订单步骤内部类，用于测试顺序消息
     */
    @Data
    private static class OrderStep{
        private long orderId;
        private Date createDate;
        private String desc;

        public OrderStep(){}

        public OrderStep(Builder builder){
            this.orderId = builder.orderId;
            this.createDate = builder.createDate;
            this.desc = builder.desc;
        }

        @Override
        public String toString(){
            return "OrderStep{"
                    + "orderId = " + orderId
                    + " , createDate = " + SIMPLE_DATE_FORMAT.format(createDate)
                    + " , desc = " + desc;
        }

        public static class Builder{
            private long orderId;
            private Date createDate;
            private String desc;

            public Builder setOrderId(long orderId){
                this.orderId = orderId;
                return this;
            }

            public Builder setCreateDate(Date date){
                this.createDate = date;
                return this;
            }

            public Builder setDesc(String desc){
                this.desc = desc;
                return this;
            }

            public OrderStep build(){
                return new OrderStep(this);
            }
        }
    }

}
