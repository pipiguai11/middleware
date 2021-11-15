package com.lhw.rocketlog.storer;

/**
 * @author ：linhw
 * @date ：21.11.15 16:45
 * @description：持久化存储接口
 * @modified By：
 */
public interface IStroe {

    String getFileName();

    boolean isFull();

    void save(String message);

}
