package com.lhw.application.controller;

import com.lhw.application.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：linhw
 * @date ：21.11.30 10:01
 * @description：
 * @modified By：
 */
@RestController
public class LogController {

    @Autowired
    ILogService service;

    @RequestMapping(value = "/log/getLog", method = RequestMethod.GET)
    public String getLog(){
        service.getLog();
        return "success";
    }

}
