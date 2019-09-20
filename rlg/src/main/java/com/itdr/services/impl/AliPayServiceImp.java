package com.itdr.services.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.itdr.common.ServerResponse;
import com.itdr.mappers.*;
import com.itdr.pojo.Order;
import com.itdr.pojo.OrderItem;
import com.itdr.pojo.pay.Configs;
import com.itdr.pojo.pay.ZxingUtils;
import com.itdr.services.AliPayService;
import com.itdr.utils.JsonUtils;
import com.itdr.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AliPayServiceImp implements AliPayService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ShippingMapper shippingMapper;
//    @Autowired
//    private PayInFoMapper payInFoMapper;


    @Override
    public ServerResponse alipay(Long orderno, Integer uid) {
        if (orderno == null || orderno <= 0){
            return ServerResponse.defeatedRs("非法参数");
        }

        //判断订单是否存在
        Order order = orderMapper.selectByOrderNo(orderno);
        if (order == null){
            return ServerResponse.defeatedRs("该订单不存在");
        }

        //判断订单和用户是否匹配
        int i = orderMapper.selectByOrderNoAndUid(orderno,uid);
        if (i <= 0 ){
            return ServerResponse.defeatedRs("订单和用户不匹配");
        }

        //根据订单号查询对应的商品详情
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());

        //调用支付宝接口获取二维码
        try {
            AlipayTradePrecreateResponse response = test_trade_precreate(order,orderItems);

            //响应成功才执行下一步
            if (response.isSuccess()){
                String filePath = String.format(Configs.getSavecode_test()+"qr-%s.png",
                        response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                Map map = new HashMap();
                map.put("orderNo", order.getOrderNo());

                map.put("qrCode", filePath);
                return ServerResponse.successRs(map);
            }else {
                return ServerResponse.defeatedRs("下单失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ServerResponse.defeatedRs("下单失败");
        }
    }

    @Override
    public ServerResponse alipayCallback(Map<String, String> newMap) {

        Long orderNo = Long.parseLong(newMap.get("out_trade_no"));

        String tarde_no = newMap.get("tarde_no ");

        String trade_status = newMap.get("trade_status");

        String payment_time = newMap.get("gmt_payment");

        BigDecimal total_amount = new BigDecimal(newMap.get("total_amount"));

        Integer uid = Integer.parseInt(newMap.get("seller_id"));

        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            return ServerResponse.defeatedRs("不是要付款的订单");
        }



        if (order.getStatus() != 10){
            return ServerResponse.defeatedRs("订单不是未付款状态");
        }

//        if (trade_status){}

        return null;
    }


    private AlipayTradePrecreateResponse test_trade_precreate(Order order, List<OrderItem> orderItems) throws AlipayApiException {
        //读取配置文件信息
        Configs.init("zfbinfo.properties");

        //实例化支付宝客户端
        AlipayClient alipayClient = new DefaultAlipayClient(Configs.getOpenApiDomain(),
                Configs.getAppid(), Configs.getPrivateKey(), "json", "utf-8",
                Configs.getAlipayPublicKey(), Configs.getSignType());

        //创建API对应的request类
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        //获取一个BizContent对象,并转换成json格式
        String str = JsonUtils.obj2String(PoToVoUtil.getBizContent(order, orderItems));
        request.setBizContent(str);
        //设置支付宝回调路径
        request.setNotifyUrl(Configs.getNotifyUrl_test());
        //获取响应,这里要处理一下异常
        AlipayTradePrecreateResponse response = alipayClient.execute(request);

        //返回响应的结果
        return response;
    }
}
