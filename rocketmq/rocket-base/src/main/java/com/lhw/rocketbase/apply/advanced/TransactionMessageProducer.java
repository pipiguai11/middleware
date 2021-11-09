package com.lhw.rocketbase.apply.advanced;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
import com.lhw.rocketbase.callback.TransactionMessageListener;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * @author ：linhw
 * @date ：21.11.3 15:25
 * @description：事务消息生产者
 *
 *      可以看到事务生产到消费的整个流程
 *      ① 先发送half消息，此时消费者不可见，所有的消息被放在了一个特殊的主题队列中（HALF_QUEUE）
 *      ② 一定时间后，消息服务器进行回查，判断HALF_QUEUE队列中所有事务消息的状态（此时的消息状态一般都是UNKNOW）
 *      ③ 通过回调的方式，调用监听器中的checkLocalTransaction方法，判断事务是否执行完毕，同时返回一个状态。
 *      ④ 当状态为Commit时，表示事务执行完毕，允许提交，这时消息服务器会把该消息从HALF_QUEUE拿出来放回原先的主题队列
 *      ⑤ 而当状态为Rollback时，表示事务执行异常，需要回滚，则该消息直接丢弃
 *      ⑥ 除了这两个状态之外，就剩下UNKNOW了，这个状态保持不变，还是留在HALF_QUEUE主题队列中，等待下一次的消息回查
 *      ⑦ 当消息回查次数达到15次时，事务还未完成，视为失败，直接丢弃
 *
 * @modified By：
 */
public class TransactionMessageProducer extends AbstractProducer implements Runnable {

    public TransactionMessageProducer(){
        super("TransactionMessageProducer");
    }

    public static void main(String[] args) {
        new Thread(new TransactionMessageProducer()).start();
    }

    @SneakyThrows
    @Override
    public void run() {
        //实例化事务监听器
        TransactionListener listener = new TransactionMessageListener();
        //定义事务生产者对象
        TransactionMQProducer producer = new TransactionMQProducer(getProducerName());
        //初始化一个线程池
        ExecutorService executorService = new ThreadPoolExecutor(5,
                10,
                100,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(2000),
                new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("local_transaction_message_exec_thread");
                return thread;
            }
        });
        producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
        producer.setTransactionListener(listener);
        producer.setExecutorService(executorService);
        producer.start();

        for (int i = 0 ; i < 10 ; i++){
            String msg = SIMPLE_DATE_FORMAT.format(new Date()) + "hello_" + i;
            Message message = new Message(getTopic(),Constant.Tag.TAG_A,msg.getBytes());
            SendResult result = producer.sendMessageInTransaction(message,"args");
            System.out.println(result);
            Thread.sleep(2000);
        }

        Scanner scanner = new Scanner(System.in);
        scanner.nextInt();

        producer.shutdown();
        finish();
    }
}
