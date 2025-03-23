package com.zyh.market.controller;
import com.zyh.market.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author <a href="https://github.com/fawu-K">fawu.K</a>
 * @since 2023-11-20 17:40
 **/

@RestController
@RequestMapping
@CrossOrigin(origins = "http://124.222.121.87:8200")
public class AliPayController {
    @Autowired
    private AlipayService alipayService;

    @GetMapping("/pay")
    public String toPay() throws Exception {
        String form = alipayService.toPay("人物头像", new BigDecimal("100.00"));
        return form;
    }
}
