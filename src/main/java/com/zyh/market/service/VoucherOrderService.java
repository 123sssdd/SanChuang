package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.entity.ProductVoucher;
import com.zyh.market.entity.VoucherOrder;
import com.zyh.market.model.R;

public interface VoucherOrderService extends IService<VoucherOrder> {
  void seckill(String voucherId);
  
  void createVoucherId(String voucherId, String userId);
}
