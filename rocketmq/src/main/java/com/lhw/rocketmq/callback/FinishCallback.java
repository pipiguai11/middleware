package com.lhw.rocketmq.callback;

/**
 * @author ：linhw
 * @date ：21.10.29 14:57
 * @description：结束任务时执行的回调
 * @modified By：
 */
public interface FinishCallback<T> {
    void callback(T msg);
}
