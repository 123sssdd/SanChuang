package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.entity.ProductCollect;
import com.zyh.market.entity.ProductOrder;
import com.zyh.market.model.R;

public interface ProductCollectService extends IService<ProductCollect> {
  R delete(String id);
}
