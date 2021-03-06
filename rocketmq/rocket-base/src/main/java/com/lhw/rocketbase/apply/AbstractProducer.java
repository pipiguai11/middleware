package com.lhw.rocketbase.apply;

import com.lhw.rocketbase.base.Constant;
import com.lhw.rocketbase.callback.FinishListener;
import com.lhw.rocketbase.callback.ProducerFinishListener;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * @author ：linhw
 * @date ：21.10.29 15:13
 * @description：抽象生产者
 * @modified By：
 */
@Data
public abstract class AbstractProducer implements Runnable{

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String producerName;

    private String topic = Constant.Topic.MY_TOPIC;

    private String tag;

    public AbstractProducer(){}

    public AbstractProducer(String producerName){
        this.producerName = producerName;
    }

    public AbstractProducer(String producerName, String topic, String tag){
        this.producerName = producerName;
        this.topic = topic;
        this.tag = tag;
    }

    public abstract void runTask();

    @Override
    public void run(){
        runTask();
        finish();
    }

    //监听器
    protected FinishListener listener = new ProducerFinishListener(this);

    public void finish(){
        listener.finish(msg -> System.out.println("【" + msg + "】消息生产者停止生产消息了"));
    }

}
