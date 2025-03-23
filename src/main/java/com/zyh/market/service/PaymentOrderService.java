package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.entity.PaymentOrder;
import com.zyh.market.model.dto.SystemPaymentOrderPageDto;

import java.util.Map;

public interface PaymentOrderService extends IService<PaymentOrder> {
  
  
  String createPaymentOrder(String orderUuid);
  
  void taskPaymentOrderPayTrue(PaymentOrder obj);
  
  Page getPaymentOrderList(SystemPaymentOrderPageDto dto);
  
  Map getOrderDetail(String id);
}
