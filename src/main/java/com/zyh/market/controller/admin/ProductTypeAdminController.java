package com.zyh.market.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.entity.ProductType;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.model.R;
import com.zyh.market.model.dto.SystemProductTypePageDto;
import com.zyh.market.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/product/type")
@SaCheckLogin
@SaCheckRole(value = {"system"})
public class ProductTypeAdminController {
  @Autowired
  private ProductTypeService productTypeService;

  @GetMapping("/page")
  public R<Page> getTypePageList(SystemProductTypePageDto dto) {
    Page page = productTypeService.getTypePageList(dto);
    return R.ok(page);
  }
  @PostMapping
  public R createType(@RequestBody ProductType type){
    if(StrUtil.isEmpty(type.getTypeCode()) || StrUtil.isEmpty(type.getTypeName()) )
      throw new ServiceException(ResultCode.ValidateError);
    ProductType productType = new ProductType();
    productType.setTypeCode(type.getTypeCode());
    productType.setTypeName(type.getTypeName());
    productType.setCreateTime(new Date().getTime());
    productType.setUpdateTime(new Date().getTime());
    boolean save = productTypeService.save(productType);
    if(!save) throw new ServiceException(ResultCode.SaveError);
    return R.ok();
  }
  @PutMapping
  public R editType(@RequestBody ProductType type){
    if (StrUtil.isEmpty(type.getTypeName()))
      throw new ServiceException(ResultCode.ValidateError);
    ProductType productType = productTypeService.getById(type.getId());
    if(BeanUtil.isEmpty(productType)) throw new ServiceException(ResultCode.NotFindError);
    productType.setTypeName(type.getTypeName());
    productType.setUpdateTime(new Date().getTime());
    boolean update = productTypeService.updateById(productType);
    if (!update) throw new ServiceException(ResultCode.UpdateError);
    return R.ok();
  }
}
