package com.lhw.rocketlog.constant;

import java.text.SimpleDateFormat;

/**
 * @author ：linhw
 * @date ：21.11.9 10:53
 * @description：常量类
 * @modified By：
 */
public class BaseConstant {

    public static final String DEFAULT_NAMESRV_ADDR = "localhost:9876";

    public static class Producer{
        public static final String MY_SYNC_PRODUCER = "mySyncProducer";
        public static final String MY_ASYNC_PRODUCER = "myAsyncProducer";
        public static final String MY_ONE_WAY_PRODUCER = "myOneWayProducer";
    }

    public static class Consumer{
        public static final String MY_DEFAULT_CONSUMER = "defaultConsumer";

        public static final String DEFAULT_TOPIC = "MyTopic";
        public static final String DEFAULT_TAG = "*";
    }

    public static class Store{
        public static final String LOCAL_STORE = "localStore";
    }

    public static class Properties{
        public static final String PREFIX = "rocket.log";

        public static final String ENABLE = PREFIX + ".enable";

        public static final String LEVEL_INFO = "info";
        public static final String LEVEL_DEBUG = "debug";
        public static final String LEVEL_WARN = "warn";
        public static final String LEVEL_ERROR = "error";
    }

    public static class Condition{
        public static final String TRUE = "true";
        public static final String FALSE = "false";
    }

    public static class Time{
        public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    }

}
