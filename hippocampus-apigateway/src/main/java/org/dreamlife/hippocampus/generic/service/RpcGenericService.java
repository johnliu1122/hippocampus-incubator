package org.dreamlife.hippocampus.generic.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/4/1
 */
public interface RpcGenericService {
    Object invoke(String interfaceClass, String methodName, String group,String version, List<String> parameterTypes, List<Object> parameters) throws Exception;
}
