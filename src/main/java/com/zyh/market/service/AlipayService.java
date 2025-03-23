package com.zyh.market.service;

import java.math.BigDecimal;

public interface AlipayService {
    String toPay(String subject, BigDecimal money) throws Exception;
}