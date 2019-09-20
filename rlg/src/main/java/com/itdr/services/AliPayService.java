package com.itdr.services;

import com.itdr.common.ServerResponse;

import java.util.Map;

public interface AliPayService {
    ServerResponse alipay(Long orderno, Integer uid);

    ServerResponse alipayCallback(Map<String,String> newMap);
}
