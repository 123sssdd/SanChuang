package com.zyh.market.model.dto;

import com.zyh.market.model.Page;
import lombok.Data;

@Data
public class UserAdminPageDto extends Page {
  private String key;
  private Integer checkStatus;
}
