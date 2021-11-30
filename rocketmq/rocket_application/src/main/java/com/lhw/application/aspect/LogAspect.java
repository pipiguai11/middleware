package com.lhw.application.aspect;

import com.alibaba.fastjson.JSONObject;
import com.lhw.rocketlog.constant.BaseConstant;
import com.lhw.rocketlog.producer.IProducer;
import com.lhw.rocketlog.producer.ProducerManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：linhw
 * @date ：21.11.30 10:05
 * @description：日志切面
 * @modified By：
 */
@Component
@Aspect
@Slf4j
@DependsOn("applicationManager")  //依赖ApplicationManager对象，等它加载完之后再加载这个切面
public class LogAspect {

    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private HttpServletRequest request;

    private IProducer producer;

    public LogAspect(){
        ProducerManager manager = new ProducerManager();
        producer = manager.getDefaultProducer();
    }

    @Pointcut("this(com.lhw.application.service.LogService)")
    public void logService(){}

    @Around("logService()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        System.out.println("环绕通知-----日志输出");
        Object result = point.proceed();

        Message message = getMessage(point);
        producer.send(message);

        return result;
    }

    /**
     * 构建一个message对象，用于消息发送
     * @param point
     * @return
     * @throws NoSuchMethodException
     */
    private Message getMessage(ProceedingJoinPoint point) throws NoSuchMethodException {
        Message message = new Message();
        StringBuilder sb = new StringBuilder();
        Object target = point.getTarget();
        Signature sig = point.getSignature();
        if (sig instanceof MethodSignature) {
            MethodSignature msig = (MethodSignature) sig;
            Method method = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            sb.append("\n+---------- start ----------+");
            sb.append(StringUtils.rightPad("\n|-referer", 12, ' ')).append(" : ").append(request.getHeader("referer"));
            sb.append(StringUtils.rightPad("\n|-ip", 12, ' ')).append(" : ").append(this.getIpAddr(request));
            sb.append(StringUtils.rightPad("\n|-uri", 12, ' ')).append(" : ").append(request.getRequestURI());
            sb.append(StringUtils.rightPad("\n|-method", 12, ' ')).append(" : ").append(request.getMethod());
            sb.append(StringUtils.rightPad("\n|-detail", 12, ' ')).append(" :");
            sb.append(StringUtils.rightPad("\n|-time", 12, ' ')).append(" :").append(SIMPLE_DATE_FORMAT.format(new Date()));
            sb.append(StringUtils.rightPad("\n|  -class", 12, ' ')).append(" :").append(target.getClass().getName());
            sb.append(StringUtils.rightPad("\n|  -method", 12, ' ')).append(" :").append(method.getName());
            JSONObject params = new JSONObject();
            String[] paramNames = msig.getParameterNames();
            if (paramNames.length > 0) {
                Object[] args = point.getArgs();
                for (int i = 0; i < paramNames.length; i++) {
                    if (!(args[i] instanceof HttpServletRequest)
                            && !(args[i] instanceof HttpServletResponse)) {
                        params.put(paramNames[i], args[i]);
                    }
                }
                sb.append(StringUtils.rightPad("\n|  -params", 12, ' ')).append(" :");
                params.forEach((key, value) -> sb.append("\n|    -").append(key).append(" : ").append(value));
            }
        }
        message.setTopic(BaseConstant.Consumer.DEFAULT_TOPIC);
        message.setBody(sb.toString().getBytes());
        return message;
    }

    /**
     * 获取客户端IP地址
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {

        String unknown = "unknown";

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error(e.getMessage());
                }

            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15 && ip.indexOf(',') > 0) {
            ip = ip.substring(0, ip.indexOf(','));
        }
        return ip;
    }

}
