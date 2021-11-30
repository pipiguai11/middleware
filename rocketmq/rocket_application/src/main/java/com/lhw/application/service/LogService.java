package com.lhw.application.service;

import org.springframework.stereotype.Service;

/**
 * @author ：linhw
 * @date ：21.11.30 10:03
 * @description：
 * @modified By：
 */
@Service
public class LogService implements ILogService{

    @Override
    public void getLog() {
        System.out.println("调用了getLog方法");
    }
}
