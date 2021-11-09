package com.lhw.rocketbase.base;

import org.apache.rocketmq.client.producer.LocalTransactionState;

/**
 * @author ：linhw
 * @date ：21.11.3 15:28
 * @description：事务状态枚举
 * @modified By：
 */
public enum TransactionStateEnum {

    UNKNOW(0,LocalTransactionState.UNKNOW),
    COMMIT(1,LocalTransactionState.COMMIT_MESSAGE),
    ROLLBACK(2,LocalTransactionState.ROLLBACK_MESSAGE);

    private Integer id;
    private LocalTransactionState state;

    TransactionStateEnum(){}

    TransactionStateEnum(int i, LocalTransactionState unknow) {
        this.id = i;
        this.state = unknow;
    }

    public LocalTransactionState getState(){
        return state;
    }

    public static LocalTransactionState getById(int id){
        for (TransactionStateEnum value : values()) {
            if (id == value.id){
                return value.state;
            }
        }
        throw new IllegalArgumentException("不存在的状态值【" + id +  "】");
    }
}
