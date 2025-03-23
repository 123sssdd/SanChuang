package com.zyh.market.task;


import com.zyh.market.Att;
import com.zyh.market.entity.PaymentOrder;
import com.zyh.market.service.PaymentOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class PaymentOrderTask {
  
  @Autowired
  private PaymentOrderService paymentOrderService;
  

  
  @Scheduled(cron = "* * * * * *")
  public void taskPaymentOrderPayTrue() {
    try {
      List<PaymentOrder> objs = paymentOrderService.query().eq(Att.PaymentOrder.processStatus, 1).eq(Att.PaymentOrder.paymentStatus, 9).list();
      for (PaymentOrder obj : objs) {
        try {
          paymentOrderService.taskPaymentOrderPayTrue(obj);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
