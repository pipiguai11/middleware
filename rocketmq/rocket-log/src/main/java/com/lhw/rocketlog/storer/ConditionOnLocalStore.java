package com.lhw.rocketlog.storer;

import com.lhw.rocketlog.config.ApplicationManager;
import com.lhw.rocketlog.constant.BaseConstant;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ：linhw
 * @date ：21.11.15 16:27
 * @description：本地持久化对象初始化条件判断
 * @modified By：
 */
public class ConditionOnLocalStore implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String enable = ApplicationManager.getApplicationContext().getEnvironment().getProperty(BaseConstant.Properties.ENABLE);
        return BaseConstant.Condition.TRUE.equals(enable);
    }
}
