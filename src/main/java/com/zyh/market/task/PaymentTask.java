package com.zyh.market.task;


import com.zyh.market.Att;
import com.zyh.market.entity.PaymentPay;
import com.zyh.market.service.PaymentOrderService;
import com.zyh.market.service.PaymentPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class PaymentTask {
  @Autowired
  private PaymentPayService paymentPayService;
  
  @Autowired
  private PaymentOrderService paymentOrderService;
  
  @Scheduled(cron = "* * * * * *")
  public void taskPaymentPayTrue() {
    try {
      List<PaymentPay> objs = paymentPayService.query().eq(Att.PaymentPay.processStatus, 1).eq(Att.PaymentPay.paymentStatus, 9).list();
      for (PaymentPay obj : objs) {
        try {
          paymentPayService.taskPaymentPayTrue(obj);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
