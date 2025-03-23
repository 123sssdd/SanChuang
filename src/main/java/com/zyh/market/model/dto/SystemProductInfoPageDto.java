package com.zyh.market.model.dto;

import com.zyh.market.model.Page;
import lombok.Data;

@Data
public class SystemProductInfoPageDto extends Page {
  private String key;
  private String status;
}
