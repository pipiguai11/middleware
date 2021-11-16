package com.lhw.rocketlog.storer;

import com.lhw.rocketlog.constant.BaseConstant;
import com.lhw.rocketlog.utils.FileUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * @author ：linhw
 * @date ：21.11.15 16:05
 * @description：本地存储
 * @modified By：
 */
@Data
@Component
@Conditional(ConditionOnLocalStore.class)
@ConfigurationProperties(prefix = BaseConstant.Properties.PREFIX)
public class LocalStore implements IStroe{

    //文件存储的根地址
    private String localBasePath;
    //日志等级
    private String level;
    //每个文件大小
    private long size;
    private boolean enable;

    private static String fileName = BaseConstant.Time.SIMPLE_DATE_FORMAT.format(new Date());
    private boolean pathExist = false;
    private boolean fileExist = false;

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean isFull(long newMessageSize) {
        long temp = FileUtil.getFileSize(localBasePath + FileUtil.SEPARATOR + fileName);
        if ((temp + newMessageSize) > size){
            return true;
        }
        return false;
    }

    public void changeFile(){
        LocalStore.fileName = BaseConstant.Time.SIMPLE_DATE_FORMAT.format(new Date());
    }

    @Override
    public void save(String message) {
        if (!pathExist){
            File basePath = new File(localBasePath);
            if (!basePath.exists()){
                FileUtil.createDir(localBasePath);
            }
            pathExist = true;
        }

        String realFilePath = localBasePath + FileUtil.SEPARATOR + fileName;
        System.out.println("文件真实路径" + realFilePath);
        if (!fileExist){
            File file = new File(realFilePath);
            if (!file.exists()){
                FileUtil.create(realFilePath);
            }
            fileExist = true;
        }

        if (isFull(message.getBytes().length)){
            changeFile();
        }
        FileUtil.writeFileV5(realFilePath, message);
    }
}
