package com.zyh.market.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.zyh.market.service.AlipayService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="https://github.com/fawu-K">fawu.K</a>
 * @since 2023-11-20 17:40
 **/

@Service
public class AlipayServiceImpl implements AlipayService {

    /**
     * 生成支付表单
     * @param subject
     * @param money
     * @return
     * @throws Exception
     */
    public String toPay(String subject, BigDecimal money) throws Exception {
        // 最后一个参数是支付完成之后跳转到的界面, 一般为项目的首页
        AlipayTradePagePayResponse pay = Factory.Payment.Page().pay(subject, this.generateTradeNo(),
                String.valueOf(money), "http://124.222.121.87:8200");
        String payForm = null;
        if (ResponseChecker.success(pay)) {
            payForm = pay.getBody();
        }
        return payForm;
    }

    /**
     * 通过时间生成外部订单号 out_trade_no
     * @return
     */
    private String generateTradeNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String tradeNo = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        return tradeNo;
    }
}
