package com.lhw.rocketlog.producer;

import org.apache.rocketmq.common.message.Message;

import java.util.List;

/**
 * @author ：linhw
 * @date ：21.11.8 15:27
 * @description：生产者抽象接口
 * @modified By：
 */
public interface IProducer {

    /**
     * 启动生产者
     */
    void start();

    /**
     * 关闭生产者
     */
    void shutdown();

    /**
     * 发送单条消息
     * @param message
     */
    void send(Message message);

    /**
     * 发送批量消息
     * @param messages
     */
    void sendAll(List<Message> messages);

}
