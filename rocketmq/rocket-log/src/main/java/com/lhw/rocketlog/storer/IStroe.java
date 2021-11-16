package com.lhw.rocketlog.storer;

/**
 * @author ：linhw
 * @date ：21.11.15 16:45
 * @description：持久化存储接口
 * @modified By：
 */
public interface IStroe {

    String getFileName();

    /**
     * 判断当前文件能否继续添加内容
     * @param newMessageSize
     * @return
     */
    boolean isFull(long newMessageSize);

    void save(String message);

}
