package com.lhw.rocketmq.callback;

/**
 * @author ：linhw
 * @date ：21.10.29 15:05
 * @description：任务结束监听器
 * @modified By：
 */
public interface FinishListener {
    void finish(FinishCallback callback);
}
