package com.lhw.rocketlog.producer;

import com.lhw.rocketlog.config.ApplicationManager;
import com.lhw.rocketlog.constant.BaseConstant;
import com.lhw.rocketlog.constant.ProducerEnum;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


/**
 * @author ：linhw
 * @date ：21.11.8 15:33
 * @description：生产者管理器
 * @modified By：
 */
@Component("producerManager")
@DependsOn("applicationManager")  //依赖ApplicationManager对象，等它加载完之后再加载这个切面
public class ProducerManager {

    public IProducer getProducerByBeanName(String producerBeanName){
        ProducerEnum.checkProducerExist(producerBeanName);
        return (IProducer)ApplicationManager.getBean(producerBeanName);
    }

    public IProducer getDefaultProducer(){
        IProducer producer = (IProducer)ApplicationManager.getBean(BaseConstant.Producer.MY_ASYNC_PRODUCER);
        producer.start();
        return producer;
    }

    public void startProducer(IProducer producer){
        producer.start();
    }

    public void shutdownProducer(IProducer producer){
        producer.shutdown();
    }

}
