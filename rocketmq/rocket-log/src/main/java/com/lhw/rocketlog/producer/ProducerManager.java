package com.lhw.rocketlog.producer;

import com.lhw.rocketlog.config.ApplicationManager;
import com.lhw.rocketlog.constant.BaseConstant;
import com.lhw.rocketlog.constant.ProducerEnum;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author ：linhw
 * @date ：21.11.8 15:33
 * @description：生产者管理器
 * @modified By：
 */
@Component("producerManager")
@DependsOn("applicationManager")  //依赖ApplicationManager对象，等它加载完之后再加载这个生产者控制器
public class ProducerManager {

    private ConcurrentHashMap<IProducer,Boolean> startedProducer = new ConcurrentHashMap<>();

    public IProducer getProducerByBeanName(String producerBeanName){
        ProducerEnum.checkProducerExist(producerBeanName);
        return (IProducer)ApplicationManager.getBean(producerBeanName);
    }

    public IProducer getDefaultProducer(){
        IProducer producer = getProducerByBeanName(BaseConstant.Producer.MY_ASYNC_PRODUCER);
        if (startedProducer.containsKey(producer) && startedProducer.get(producer)){
            return producer;
        }

        startProducer(producer);
        return producer;
    }

    public void startProducer(IProducer producer){
        producer.start();
        startedProducer.put(producer,true);
    }

    public void shutdownProducer(IProducer producer){
        producer.shutdown();
        startedProducer.put(producer,false);
    }

}
