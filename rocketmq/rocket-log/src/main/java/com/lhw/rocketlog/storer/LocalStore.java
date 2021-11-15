package com.lhw.rocketlog.storer;

import com.lhw.rocketlog.constant.BaseConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author ：linhw
 * @date ：21.11.15 16:05
 * @description：本地存储
 * @modified By：
 */
@Data
@Configuration
@Conditional(ConditionOnLocalStore.class)
public class LocalStore implements IStroe{

    private String localBasePath;
    private String level;

    private static String fileName = BaseConstant.Time.SIMPLE_DATE_FORMAT.format(new Date());

    @Bean(BaseConstant.Store.LOCAL_STORE)
    @ConfigurationProperties(prefix = BaseConstant.Properties.PREFIX)
    public LocalStore initLocalStore(){
        return new LocalStore();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void save(String message) {

    }
}
