package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.entity.ProductOrder;
import com.zyh.market.entity.ProductType;
import com.zyh.market.model.dto.SystemProductTypePageDto;

public interface ProductTypeService extends IService<ProductType> {
  Page getTypePageList(SystemProductTypePageDto dto);
}
