package com.lhw.rocketlog.consumer;

/**
 * @author ：linhw
 * @date ：21.11.15 17:17
 * @description：消费者抽象接口
 * @modified By：
 */
public interface IConsumer {

    void start();

    void shutdown();

}
