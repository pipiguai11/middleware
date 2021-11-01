package com.lhw.rocketmq.base;

/**
 * @author ：linhw
 * @date ：21.10.29 14:30
 * @description：常量类
 * @modified By：
 */
public class Constant {

    public static final String DEFAULT_NAMESRV_ADDR = "localhost:9876";

    public static class Topic{
        public static final String DEFAULT_TOPIC_NAME = "testTopic";
        public static final String MY_TOPIC = "MyTopic";
        public static final String ASYNC_TOPIC = "AsyncTopic";
        public static final String ONE_WAY_TOPIC = "oneWayTopic";
    }

    public static class Tag{
        public static final String TAG_A = "TagA";
        public static final String TAG_B = "TagB";
        public static final String TAG_C = "TagC";
        public static final String TAG_D = "TagD";
        public static final String TAG_A_C_D = "TagA || TagC || TagD";
        public static final String ALL_TAG = "*";
    }

}
