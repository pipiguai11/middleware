package com.lhw.rocketlog.constant;

public enum ProducerEnum {

    SYNC_PRODUCER(1,BaseConstant.Producer.MY_SYNC_PRODUCER),
    ASYNC_PRODUCER(2,BaseConstant.Producer.MY_ASYNC_PRODUCER),
    ONE_WAY_PRODUCER(3,BaseConstant.Producer.MY_ONE_WAY_PRODUCER);

    public int id;
    public String producerBeanName;

    ProducerEnum(int id, String producerBeanName){
        this.id = id;
        this.producerBeanName = producerBeanName;
    }

    public static String getProducerNameById(int id){
        for (ProducerEnum p : values()){
            if (p.id == id){
                return p.producerBeanName;
            }
        }
        throw new IllegalArgumentException("【" + id + "】生产者不存在，请检查");
    }

    public static void checkProducerExist(String producerBeanName){
        for (ProducerEnum p : values()){
            if (p.producerBeanName.equals(producerBeanName)){
                return;
            }
        }
        throw new IllegalArgumentException("【" + producerBeanName + "】生产者不存在，请检查");
    }

}
