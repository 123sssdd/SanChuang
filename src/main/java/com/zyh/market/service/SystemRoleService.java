package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.entity.SystemRole;
import com.zyh.market.model.dto.SystemRolePageDto;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/19 20:21
 */
public interface SystemRoleService extends IService<SystemRole> {
  Page getRolePageList(SystemRolePageDto rolePageDto);
}
