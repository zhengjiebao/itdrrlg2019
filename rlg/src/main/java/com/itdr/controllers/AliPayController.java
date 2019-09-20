package com.itdr.controllers;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.pojo.pay.Configs;
import com.itdr.services.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
@ResponseBody
@RequestMapping("/order/")
public class AliPayController {

    @Autowired
    AliPayService aliPayService;

    @RequestMapping("pay.do")
    private ServerResponse alipay(Long orderno, HttpSession session){
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }
        return aliPayService.alipay(orderno,users.getId());
    }

    @RequestMapping("alipay_callback")
    private String alipayCallback(HttpServletRequest request, HttpServletResponse response){

        //获取支付宝返回的参数，返回一个map集合
        Map<String, String[]> parameterMap = request.getParameterMap();

        //获取上面集合的键的set集合
        Set<String> strings = parameterMap.keySet();

        //使用迭代器遍历集合获得值
        Iterator<String> iterator = strings.iterator();

        //创建一个接收参数的集合
        Map<String ,String> newMap = new HashMap<>();

        //遍历迭代器重新组装参数
        while (iterator.hasNext()){
            //根据键获取parameterMap值
            String key = iterator.next();
            String[] strings1 = parameterMap.get(key);
            //遍历值的数组，重新拼装数据
            StringBuffer stringBuffer = new StringBuffer("");
            for (int i = 0; i < strings1.length; i++) {
                stringBuffer = (i == strings1.length - 1) ? stringBuffer.append(strings1[i]):stringBuffer.append(strings1[i]+",");
            }
            //把新的数据以键值对的方式放入一个新的集合中
            newMap.put(key,stringBuffer.toString());
        }

        //去除不必要参数
        newMap.remove("sign_type");

        try {
            //使用官方验签方法验证
            boolean b = AlipaySignature.rsaCheckV2(newMap, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            //验证通过执行下一步
            if (!b){
                return "{'msg':'验签失败'}";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "{'msg':'验签失败'}";
        }

        //验签通过，去业务层执行业务
        ServerResponse sr = aliPayService.alipayCallback(newMap);

        //业务层处理完，返回对应的状态信息
        if (sr.isSuccess()){
            return "SUCCESS";
        }else {
            return "FAILED";
        }
    }
}
