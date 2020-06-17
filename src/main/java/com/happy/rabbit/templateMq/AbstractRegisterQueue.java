package com.happy.rabbit.templateMq;

import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author happy
 * 注册队列并设置监听
 */
@Data
public class AbstractRegisterQueue {

    public final Log logger= LogFactory.getLog(this.getClass());



}
