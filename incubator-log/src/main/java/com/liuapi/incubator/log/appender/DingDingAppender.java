package com.liuapi.incubator.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.google.common.base.Strings;
import lombok.Data;
import com.liuapi.incubator.httpclient4.HttpUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * 钉钉消息通知
 * 需要在 logback-spring.xml中配置该信息
 *
 * <configuration>
 *      <property name="FILE_LOG_PATTERN"
 *               value="%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}"/>
 *      <appender name="DINGDING" class="org.dreamlife.hippocampus.log.appender.DingDingAppender">
 *         <layout class="ch.qos.logback.classic.PatternLayout" >
 *             <pattern>${FILE_LOG_PATTERN}</pattern>
 *         </layout>
 *         <webHook>钉钉机器人链接</webHook>
 *         <mobiles>手机号</mobiles>
 *         <filter class="ch.qos.logback.classic.filter.LevelFilter">
 *             <level>ERROR</level>
 *             <onMatch>ACCEPT</onMatch>
 *             <onMismatch>DENY</onMismatch>
 *         </filter>
 *      </appender>
 *  </configuration>
 *
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/4/11
 */
@Data
public class DingDingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private Layout<ILoggingEvent> layout;
    private String webHook;
    private String mobiles;

    @Override
    public void start() {
        if (this.layout == null) {
            addError("No layout set for the appender named [" + name + "].");
            return;
        }
        if (Strings.isNullOrEmpty(webHook)) {
            addError("parameters[webHook] missing. Cannot start.");
            return;
        }
        try {
            new URI(webHook);
        } catch (URISyntaxException x) {
            addError("parameters[webHook] is not a url. Cannot start.");
            return;
        }
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        String msg = layout.doLayout(eventObject);
        handle(msg);
    }

    public void handle(String msg){
        String readyToSendMsg = templateText(msg,mobiles);
        try {
            HttpUtil.doPostWithJson(webHook,readyToSendMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 1) 消息不支持\" 必须输入 \" 否则会有异常
     * 2) 如果手机号在群中不存在，则请求任然成功，只是不会@群成员
     * @return
     */
    private String templateText(String msg,String mobiles) {
        msg = msg.replaceAll("\"", "\\\\\"");
        String textMsg = "{ " + "\"msgtype\": \"text\", " + "\"text\": {\"content\":\"" + msg + "\"}," + " \"at\": {"
                + "\"atMobiles\":[\"" + mobiles + "\"],\"isAtAll\":false}" + "}";
        return textMsg;
    }
}
