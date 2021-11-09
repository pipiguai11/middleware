package com.lhw.rocketbase.callback;

import com.lhw.rocketbase.base.TransactionStateEnum;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：linhw
 * @date ：21.11.3 15:28
 * @description：事务消息监听器
 *
 *      监听本地事务是否执行完毕
 *
 * @modified By：
 */
public class TransactionMessageListener implements TransactionListener {

    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    @SneakyThrows
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        System.out.println("开始执行事务任务");
        System.out.println("当前消息：body：" + new String(msg.getBody()));
        System.out.println("执行本地事务需要的参数：" + arg);
        Integer value = transactionIndex.getAndIncrement();
        Integer status = value % 3;
        localTrans.put(msg.getTransactionId(),status);
        System.out.println("事务任务执行完毕");
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Integer status = localTrans.get(msg.getTransactionId());
        System.out.println("开始检查本地事务状态，当前事务id为：【" + msg.getTransactionId() + "】，消息内容：【" + new String(msg.getBody()) + "】，状态为：【" + status + "】");
        if ( null != status){
            return TransactionStateEnum.getById(status);
        }
        return TransactionStateEnum.COMMIT.getState();
    }
}
