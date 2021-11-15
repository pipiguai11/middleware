//package com.lhw.rocketlog.config;
//
//import com.lhw.rocketlog.constant.BaseConstant;
//import com.lhw.rocketlog.producer.AsyncProducer;
//import com.lhw.rocketlog.producer.IProducer;
//import com.lhw.rocketlog.producer.OneWayProducer;
//import com.lhw.rocketlog.producer.SyncProducer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author ：linhw
// * @date ：21.11.9 10:55
// * @description：生产者配置类
// * @modified By：
// */
//@Configuration
//public class ProducerConfig {
//
//    @Bean(BaseConstant.Producer.MY_SYNC_PRODUCER)
//    public IProducer syncProducerConfig(){
//        return new SyncProducer();
//    }
//
//    @Bean(BaseConstant.Producer.MY_ASYNC_PRODUCER)
//    public IProducer asyncProducerConfig(){
//        return new AsyncProducer();
//    }
//
//    @Bean(BaseConstant.Producer.MY_ONE_WAY_PRODUCER)
//    public IProducer oneWayProducerConfig(){
//        return new OneWayProducer();
//    }
//
//}
