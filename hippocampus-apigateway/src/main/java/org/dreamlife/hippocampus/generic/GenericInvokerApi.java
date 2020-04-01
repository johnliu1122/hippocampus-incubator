package org.dreamlife.hippocampus.generic;

import org.dreamlife.hippocampus.generic.model.GenericInvokeQO;
import org.dreamlife.hippocampus.generic.service.RpcGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/4/1
 */
@RestController
public class GenericInvokerApi {
    @Autowired
    private RpcGenericService rpcGenericService;

    @PostMapping("/api/gateway.do")
    public Object invoke(@RequestBody GenericInvokeQO genericInvokeQO) {
        try{
            List<String> parameterTypes = genericInvokeQO.getParameterTypes();
            List<Object> parameters = genericInvokeQO.getParameters();
            if(null == parameterTypes){
                parameterTypes = Collections.emptyList();
            }
            if(null == parameters){
                parameters = Collections.emptyList();
            }
            if(parameters.size()!=parameterTypes.size()){
                return "参数错误： parameterTypes为所有参数的全限类名列表，parameter为所有参数的列表";
            }

            return rpcGenericService.invoke(genericInvokeQO.getInterfaceClass(),
                    genericInvokeQO.getMethodName(),
                    genericInvokeQO.getGroup(),
                    genericInvokeQO.getVersion(),
                    parameterTypes,
                    parameters);
        }catch (Exception e){
            // 简单点，直接返回错误信息
            return e.getMessage();
        }



    }
}
